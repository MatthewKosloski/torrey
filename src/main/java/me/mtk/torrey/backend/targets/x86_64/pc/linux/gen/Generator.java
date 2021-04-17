package me.mtk.torrey.backend.targets.x86_64.pc.linux.gen;

import java.util.Map;
import java.util.Queue;
import java.util.HashMap;
import java.util.LinkedList;
import me.mtk.torrey.frontend.ir.instructions.Quadruple;
import me.mtk.torrey.frontend.ir.instructions.CopyInst;
import me.mtk.torrey.frontend.ir.instructions.GotoInst;
import me.mtk.torrey.frontend.ir.instructions.IfInst;
import me.mtk.torrey.frontend.ir.instructions.LabelInst;
import me.mtk.torrey.frontend.ir.instructions.UnaryInst;
import me.mtk.torrey.frontend.ir.instructions.BinaryInst;
import me.mtk.torrey.frontend.ir.instructions.ParamInst;
import me.mtk.torrey.frontend.ir.instructions.CallInst;
import me.mtk.torrey.frontend.ir.addressing.Address;
import me.mtk.torrey.frontend.ir.addressing.TempAddress;
import me.mtk.torrey.frontend.ir.addressing.ConstAddress;
import me.mtk.torrey.frontend.ir.gen.IRProgram;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.AddressingMode;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.BaseRelative;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.Register;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.Temporary;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.X86Address;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.Immediate;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.LabelAddress;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.Global;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions.X86Inst;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions.Callq;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions.Cmp;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions.ConditionCode;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions.Cqo;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions.Jcc;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions.Jmp;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions.Label;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions.Idivq;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions.Imulq;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions.Movq;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions.Negq;

/**
 * Generates 64-bit x86 assembly code
 * from three-address code represented by
 * a collection of quadruples of the form
 * (op, arg1, arg2, result).
 */
public final class Generator 
{
    
    // The IR program from which x86 code will be generated.
    private IRProgram ir;

    // An x86 program that is equivalent to the input IR program.
    private X86Program x86;

    // The temp names pointing to parameters of subsequent call instructions.
    private Queue<String> params;

    public Generator(IRProgram ir)
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
            else if (quad instanceof IfInst)
                gen((IfInst) quad);
            else if (quad instanceof LabelInst)
                gen((LabelInst) quad);
            else if (quad instanceof GotoInst)
                gen((GotoInst) quad);
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
                x86.instrs().add(i, new Movq(inst.arg1(), 
                    new Register("%r10")));

                // Update the arg1 of this instruction to
                // be %r10
                inst.setArg1(new Register("%r10"));
            }
        }

        return x86;
    }

    private void gen(IfInst inst)
    {
        // Convert the IR operands to x86 addresses.
        final X86Address arg1 = transAddress(inst.arg1());
        final X86Address arg2 = transAddress(inst.arg2());
        
        final Register arg1Temp = new Register("%r10");
        final Register arg2Temp = new Register("%r11");
        
        // move the operands to temporary registers to
        // perform the comparison.
        x86.addInst(new Movq(arg1, arg1Temp));
        x86.addInst(new Movq(arg2, arg2Temp));

        // Perform the comparison.
        x86.addInst(new Cmp(arg2Temp, arg1Temp));

        // Conditional jump if comparison is false.
        if (inst.op().opText().equals(">="))
         x86.addInst(new Jcc(ConditionCode.JGE, 
            new LabelAddress(inst.result().toString())));
    }

    private void gen(LabelInst inst)
    {
        System.out.println();
        x86.addInst(new Label(new LabelAddress(inst.arg1().toString())));
    }

    private void gen(GotoInst inst)
    {
        System.out.println();
        x86.addInst(new Jmp(new LabelAddress(inst.arg1().toString())));
        // x86.addInst(new Label(inst.arg1()));
    }

    private void gen(CopyInst inst)
    {
        final Address srcAddr = inst.arg1();
        final Address destAddr = inst.result();

        X86Address src = null;
        final X86Address dest = new Temporary(destAddr.toString());

        if (srcAddr instanceof ConstAddress)
            src = transConstAddress((ConstAddress) srcAddr);
        else if (srcAddr instanceof TempAddress)
            src = new Temporary(srcAddr.toString());
        else
            throw new Error("X86Generator.gen(CopyInst):"
                + " Unhandled Address.");

        x86.addInst(new Movq(src, dest));
    }

    private void gen(UnaryInst inst)
    { 
        final Address srcAddr = inst.arg1();
        final Address destAddr = inst.result();

        X86Address src = null;
        final X86Address dest = new Temporary(destAddr.toString());

        if (srcAddr instanceof ConstAddress)
            src = transConstAddress((ConstAddress) srcAddr);
        else if (srcAddr instanceof TempAddress)
            src = new Temporary(srcAddr.toString());
        else
            throw new Error("X86Generator.gen(UnaryInst):"
                + " Unhandled Address.");

        x86.addInst(new Movq(src, dest));
        x86.addInst(new Negq(dest));

        // TODO: The x86 instruction corresponding to the IR instruction
        // `x = - y` is `negq y`, and thus the new destination is y. We
        // need to replace all subsequent occurrences of x with y.
    }

    private void gen(BinaryInst inst)
    {
        final String op = inst.op().opText();
        final Address arg1Addr = inst.arg1();
        final Address arg2Addr = inst.arg2();
        final Address destAddr = inst.result();

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
            x86.addInst(new Movq(arg1, dest));

            final String opcode = op == "+" ? "addq" : "subq";

            // Add or subtract the second argument
            // by the first, storing the result in dest.
            x86.addInst(new X86Inst(opcode, arg2, dest));
        }
        else if (op == "*")
        {
            // move first argument to rax register
            x86.addInst(new Movq(arg1, new Register("%rax")));

            // move second argument to rbx register
            x86.addInst(new Movq(arg2, new Register("%rbx")));

            // multiply the contents of %rax by arg2, placing the low
            // 64 bits of the product in %rax.
            x86.addInst(new Imulq(new Register("%rbx")));

            // move the product, which is in %rax, to a temp location.
            x86.addInst(new Movq(new Register("%rax"), dest));
        }
        else if (op == "/")
        {
            // move dividend to rax register
            x86.addInst(new Movq(arg1, new Register("%rax")));

            // move divisor to temp destination
            x86.addInst(new Movq(arg2, dest));

            // sign-extend %rax into %rdx. The former contains
            // the low 64 bits of dividend, the latter contains
            // the high 64 bits.
            x86.addInst(new Cqo());

            // divide %rdx:%rax by divisor, leaving result in %rax.
            x86.addInst(new Idivq(dest));

            // Move contents of %rax to destination.
            x86.addInst(new Movq(new Register("%rax"), dest));
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
                x86.addInst(new Movq(new Temporary(param), new Register("%rdi")));
                x86.addInst(new Callq(new Global("print_int")));
                if (procName.equals("println"))
                    x86.addInst(new Callq(new Global("print_nl")));
            }
        }
    }

    private Immediate transConstAddress(ConstAddress addr)
    {
        return new Immediate(String.format("$%s", addr));
    }

    /*
     * Converts the given IR address to an equivalent x86 address.
     * 
     * @param addr An IR address.
     * @return An equivalent x86 address.
     */
    private X86Address transAddress(Address addr)
    {
        if (addr instanceof ConstAddress)
            return transConstAddress((ConstAddress) addr);
        else if (addr instanceof TempAddress)
            return new Temporary(addr.toString());
        else
            throw new Error("transAddress(Address): Cannot translate.");
    }

}
