package me.mtk.torrey.x86;

import java.util.Map;
import java.util.Queue;
import java.util.HashMap;
import java.util.LinkedList;
import me.mtk.torrey.ir.Quadruple;
import me.mtk.torrey.ir.CopyInst;
import me.mtk.torrey.ir.UnaryInst;
import me.mtk.torrey.ir.BinaryInst;
import me.mtk.torrey.ir.ParamInst;
import me.mtk.torrey.ir.CallInst;
import me.mtk.torrey.ir.Address;
import me.mtk.torrey.ir.TempAddress;
import me.mtk.torrey.ir.ConstAddress;
import me.mtk.torrey.ir.IRProgram;

/**
 * Generates 64-bit x86 assembly code
 * from three-address code represented by
 * a collection of quadruples of the form
 * (op, arg1, arg2, result).
 */
public final class X86Generator 
{
    
    // The IR program from which x86 code will be generated.
    private IRProgram ir;

    // An x86 program that is equivalent to the input IR program.
    private X86Program x86;

    // The temp names pointing to parameters of subsequent call instructions.
    private Queue<String> params;

    public X86Generator(IRProgram ir)
    {
        this.ir = ir;
        this.x86 = new X86Program(ir.temps().size() * 8);
        params = new LinkedList<>();
    }

    public X86Program gen()
    {
        for (Quadruple quad : ir.quads())
        {
            if (quad instanceof CopyInst)
                gen((CopyInst) quad);
            else if (quad instanceof UnaryInst)
                gen((UnaryInst) quad);
            else if (quad instanceof BinaryInst)
                gen((BinaryInst) quad);
            else if (quad instanceof ParamInst)
                gen((ParamInst) quad);
            else if (quad instanceof CallInst)
                gen((CallInst) quad);
            else
                throw new Error("Cannot generate x86 instruction");
        }

        // At this point, we have a "pseudo-x86" program with a, potentially
        // infinite, amount of IR temporaries. We need to replace all temporaries 
        // with base-relative stack addresses (relative to the current frame's 
        // base pointer). This is not a really effective way of using the 
        // CPU to do computations, so later we will use some algorithm 
        // (e.g., graph coloring) to perform register allocation. This 
        // will make the program much faster as it does not have to make 
        // as many trips to main memory (registers only require one CPU
        // clock cycle, main memory access requires hundreds).

        // Maps a temporary (e.g., "t1") to a base-relative stack 
        // address (e.g., -8(%rbp)).
        final Map<String, String> stackAddrs = new HashMap<>();
        
        // Assign a base-relative stack address to each IR temporary.
        int offset = 0;
        for (String temp : ir.temps())
            stackAddrs.put(temp, String.format("%d(%%rbp)", offset -= 8));

        // Traverse the x86 program, replacing each IR temporary with
        // its assigned base-relative stack address.
        for (X86Inst inst : x86.instrs())
        {
            if (inst.arg1() != null && inst.arg1().mode() == AddressingMode.TEMP)
                inst.setArg1(new BaseRelative(
                    stackAddrs.get((String) inst.arg1().value())));
            if (inst.arg2() != null && inst.arg2().mode() == AddressingMode.TEMP)
                inst.setArg2(new BaseRelative(
                    stackAddrs.get((String) inst.arg2().value())));
        }

        // In x86, it is illegal for both arguments to an instruction 
        // be stack locations. Thus, if we have an instruction like
        // `movq -16(%rbp), -24(%rbp)`, we must put one of the arguments
        // in a register before perfoming movq. 
        // So...
        // Ensure that each instruction adheres to the restriction
        // that at most one argument of an instruction may be
        // a memory reference.

        for (int i = 0; i < x86.instrs().size(); i++)
        {
            final X86Inst inst = x86.instrs().get(i);

            if (inst.arg1() != null && inst.arg1().mode() 
                == AddressingMode.BASEREL &&
                inst.arg2() != null && inst.arg2().mode() == 
                AddressingMode.BASEREL)
            {
                // Both arguments are stack locations,
                // so move arg1 to register %r10 before
                // performing the instruction.
                x86.instrs().add(i, new X86Inst("movq", 
                    inst.arg1(), new Register("%r10")));

                // Update the arg1 of this instruction to
                // be %r10
                inst.setArg1(new Register("%r10"));
            }
        }

        return x86;
    }

    private void gen(CopyInst inst)
    {
        final Address srcAddr = inst.arg1();
        final TempAddress destAddr = inst.result();

        X86Address src = null;
        final X86Address dest = new Temporary(destAddr.toString());

        if (srcAddr instanceof ConstAddress)
            src = transConstAddress((ConstAddress) srcAddr);
        else if (srcAddr instanceof TempAddress)
            src = new Temporary(srcAddr.toString());
        else
            throw new Error("X86Generator.gen(CopyInst):"
                + " Unhandled Address.");

        x86.addInst(new X86Inst("movq", src, dest));
    }

    private void gen(UnaryInst inst)
    { 
        final Address srcAddr = inst.arg1();
        final TempAddress destAddr = inst.result();

        X86Address src = null;
        final X86Address dest = new Temporary(destAddr.toString());

        if (srcAddr instanceof ConstAddress)
            src = transConstAddress((ConstAddress) srcAddr);
        else if (srcAddr instanceof TempAddress)
            src = new Temporary(srcAddr.toString());
        else
            throw new Error("X86Generator.gen(UnaryInst):"
                + " Unhandled Address.");

        x86.addInst(new X86Inst("movq", src, dest));
        x86.addInst(new X86Inst("negq", dest, null));

        // TODO: The x86 instruction corresponding to the IR instruction
        // `x = - y` is `negq y`, and thus the new destination is y. We
        // need to replace all subsequent occurrences of x with y.
    }

    private void gen(BinaryInst inst)
    {
        final String op = inst.op().opText();
        final Address arg1Addr = inst.arg1();
        final Address arg2Addr = inst.arg2();
        final TempAddress destAddr = inst.result();

        X86Address arg1 = null, arg2 = null;
        final X86Address dest = new Temporary(destAddr.toString());

        if (arg1Addr instanceof ConstAddress)
            arg1 = transConstAddress((ConstAddress) arg1Addr);
        else if (arg1Addr instanceof TempAddress)
            arg1 = new Temporary(arg1Addr.toString());
        else
            throw new Error("X86Generator.gen(BinaryInst):"
                + " Unhandled Address case.");

        if (arg2Addr instanceof ConstAddress)
            arg2 = transConstAddress((ConstAddress) arg2Addr);
        else if (arg2Addr instanceof TempAddress)
            arg2 = new Temporary(arg2Addr.toString());
        else
            throw new Error("X86Generator.gen(BinaryInst):"
                + " Unhandled Address case.");

        if (op == "+" || op == "-")
        {
            // Store first argument in temp
            x86.addInst(new X86Inst("movq", arg1, dest));

            final String opcode = op == "+" ? "addq" : "subq";

            // Add or subtract the second argument
            // by the first, storing the result in dest.
            x86.addInst(new X86Inst(opcode, arg2, dest));
        }
        else if (op == "*")
        {
            // move first argument to rax register
            x86.addInst(new X86Inst("movq", arg1, new Register("%rax")));

            // move second argument to rbx register
            x86.addInst(new X86Inst("movq", arg2, new Register("%rbx")));

            // multiply te contents of %rax by arg2, placing the low
            // 64 bits of the product in %rax.
            x86.addInst(new X86Inst("imulq", new Register("%rbx"), null));

            // move the product, which is in %rax, to a temp location.
            x86.addInst(new X86Inst("movq", new Register("%rax"), dest));
        }
        else if (op == "/")
        {
            // move dividend to rax register
            x86.addInst(new X86Inst("movq", arg1, new Register("%rax")));

            // move divisor to temp destination
            x86.addInst(new X86Inst("movq", arg2, dest));

            // sign-extend %rax into %rdx. The former contains
            // the low 64 bits of dividend, the latter contains
            // the high 64 bits.
            x86.addInst(new X86Inst("cqo", null, null));

            // divide %rdx:%rax by divisor, leaving result in %rax.
            x86.addInst(new X86Inst("idivq", dest, null));

            // Move contents of %rax to destination.
            x86.addInst(new X86Inst("movq", new Register("%rax"), dest));
        }
        else 
            throw new Error("X86Generator.gen(BinaryInst):"
                + " Unhandled binary case.");
    }

    private void gen(ParamInst inst)
    {
        params.add((String) inst.arg1().value());
    }

    private void gen(CallInst inst)
    {
        final String procName = (String) inst.arg1().value();
        final int numParams = (int) inst.arg2().value();

        if (procName.equals("print") || procName.equals("println"))
        {
            for (int i = 0; i < numParams; i++)
            {
                String param = params.remove();
                x86.addInst(new X86Inst(
                    "movq",
                    new Temporary(param),
                    new Register("%rdi")));
                x86.addInst(new X86Inst(
                    "call",
                    new Global("print_int"), 
                    null));
                if (procName.equals("println"))
                {
                    x86.addInst(new X86Inst(
                        "call",
                        new Global("print_nl"), 
                        null));
                }
            }
        }
    }

    private Immediate transConstAddress(ConstAddress addr)
    {
        return new Immediate(String.format("$%s", addr));
    }

}
