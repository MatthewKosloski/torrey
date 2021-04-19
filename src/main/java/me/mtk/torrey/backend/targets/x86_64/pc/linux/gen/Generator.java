package me.mtk.torrey.backend.targets.x86_64.pc.linux.gen;

import java.util.Map;
import java.util.Queue;
import java.util.Arrays;
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
import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.Registers;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.Temporary;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.X86Address;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.Immediate;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.LabelAddress;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.Global;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions.X86Inst;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions.Addq;
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
import me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions.Popq;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions.Pushq;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions.Retq;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions.Subq;

/**
 * Generates 64-bit x86 assembly code
 * from three-address code represented by
 * a collection of quadruples of the form
 * (op, arg1, arg2, result).
 */
public final class Generator 
{
    // The name of the label of the program's prelude.
    private static final String PRELUDE_LABEL_NAME = "main";

    // The name of the label of the program's entry point.
    private static final String ENTRY_LABEL_NAME = "start";

    // The name of the label of the program's conclusion.
    private static final String CONCLUSION_LABEL_NAME = "conclusion";
    
    // The IR program from which x86 code will be generated.
    private IRProgram ir;

    // An x86 program that is equivalent to the input IR program.
    private X86Program x86;

    // The temp names pointing to parameters of subsequent call instructions.
    private Queue<String> params;

    // The program's stack size measured in bytes (a multiple of 16).
    private final int stackSize;

    public Generator(IRProgram ir)
    {
        this.ir = ir;
        this.x86 = new X86Program();
        params = new LinkedList<>();

        // By default, allocate 8 bytes per temp variable.
        final int tempStackSize = ir.temps().size() * 8;

        // Ensure the stack pointer is 16-bytes aligned
        // by setting tempStackSize to the nearest
        // multiple of 16.
        if (tempStackSize % 16 != 0)
            // tempStackSize isn't a multiple of 16,
            // so set it to the nearest multiple of 16.
            this.stackSize = closestMultiple(tempStackSize, 16);
        else
            this.stackSize = tempStackSize;
    }

    public X86Program gen()
    {
        // Add assembler directives.
        x86.addDirective(new AssemblerDirective(AssemblerDirectiveType.TEXT));
        x86.addDirective(new AssemblerDirective(AssemblerDirectiveType.GLOBL, 
            Arrays.asList(PRELUDE_LABEL_NAME)));

        final LabelAddress preludeAddr = new LabelAddress(PRELUDE_LABEL_NAME);
        final LabelAddress entryAddr = new LabelAddress(ENTRY_LABEL_NAME);
        final LabelAddress concludeAddr = new LabelAddress(CONCLUSION_LABEL_NAME);

        // Generate instructions for the program's entry point.
        x86.addInst(new Label(preludeAddr))

            // Save the caller's base pointer in our stack. The base
            // pointer points to the beginning of a stack frame. This
            // also aligns the stack pointer.
            .addInst(new Pushq(Register.RBP))

            // The top of the stack now contains the caller's (the OS's)
            // base pointer (thus %rsp points to it). Change the base
            // pointer so that it points to the location of the old
            // base pointer.
            .addInst(new Movq(Register.RSP, Register.RBP))

            // Allocate the stack by moving the stack pointer down
            // stackSize bytes (remember, the stack grows downward,
            // so we must subtract to increase the stack size).
            .addInst(new Subq(new Immediate(stackSize), Register.RSP))

            // Unconditionally jump to the start of our program.
            .addInst(new Jmp(entryAddr));

        
        // Generate the program's instructions by translating
        // IR instructions to x86_64 instructions.
        x86.addInst(new Label(entryAddr));
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

        // Unconditionally jump to the conclusion of our program.
        x86.addInst(new Jmp(concludeAddr));

        x86.addInst(new Label(concludeAddr))
            
            // Move the stack pointer up stackSize bytes to 
            // the old base pointer.
            .addInst(new Addq(new Immediate(stackSize), Register.RSP))

            // Pop the old base pointer off the stack, storing it in
            // register %rbp.
            .addInst(new Popq(Register.RBP))

            // Pop the OS's return address off the stack and jump to it.
            .addInst(new Retq());

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
                    new Register(Registers.R10)));

                // Update the arg1 of this instruction to
                // be %r10
                inst.setArg1(new Register(Registers.R10));
            }
        }

        return x86;
    }

    private void gen(IfInst inst)
    {
        // Convert the IR operands to x86 addresses.
        final X86Address arg1 = transAddress(inst.arg1());
        final X86Address arg2 = transAddress(inst.arg2());
        
        final Register arg1Temp = new Register(Registers.R10);
        final Register arg2Temp = new Register(Registers.R11);
        
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
        x86.addInst(new Label(new LabelAddress(inst.arg1().toString())));
    }

    private void gen(GotoInst inst)
    {
        x86.addInst(new Jmp(new LabelAddress(inst.result().toString())));
    }

    private void gen(CopyInst inst)
    {
        final Address srcAddr = inst.arg1();
        final Address destAddr = inst.result();

        final X86Address src = transAddress(srcAddr);
        final X86Address dest = new Temporary(destAddr.toString());

        x86.addInst(new Movq(src, dest));
    }

    private void gen(UnaryInst inst)
    { 
        final Address srcAddr = inst.arg1();
        final Address destAddr = inst.result();

        final X86Address src = transAddress(srcAddr);
        final X86Address dest = new Temporary(destAddr.toString());

        x86.addInst(new Movq(src, dest));
        x86.addInst(new Negq(dest));
    }

    private void gen(BinaryInst inst)
    {
        final String op = inst.op().opText();
        final Address arg1Addr = inst.arg1();
        final Address arg2Addr = inst.arg2();
        final Address destAddr = inst.result();

        final X86Address arg1 = transAddress(arg1Addr);
        final X86Address arg2 = transAddress(arg2Addr);
        final Temporary dest = new Temporary(destAddr.toString());

        if (op == "+" || op == "-")
        {
            // Store first argument in temp
            x86.addInst(new Movq(arg1, dest));

            // Add or subtract the second argument
            // by the first, storing the result in dest.
            if (op.equals("+"))
                x86.addInst(new Addq(arg2, dest));
            else
                x86.addInst(new Subq(arg2, dest));
        }
        else if (op == "*")
        {
            // move first argument to rax register
            x86.addInst(new Movq(arg1, new Register(Registers.RAX)));

            // move second argument to rbx register
            x86.addInst(new Movq(arg2, new Register(Registers.RBX)));

            // multiply the contents of %rax by arg2, placing the low
            // 64 bits of the product in %rax.
            x86.addInst(new Imulq(new Register(Registers.RBX)));

            // move the product, which is in %rax, to a temp location.
            x86.addInst(new Movq(new Register(Registers.RAX), dest));
        }
        else if (op == "/")
        {
            // move dividend to rax register
            x86.addInst(new Movq(arg1, new Register(Registers.RAX)));

            // move divisor to temp destination
            x86.addInst(new Movq(arg2, dest));

            // sign-extend %rax into %rdx. The former contains
            // the low 64 bits of dividend, the latter contains
            // the high 64 bits.
            x86.addInst(new Cqo());

            // divide %rdx:%rax by divisor, leaving result in %rax.
            x86.addInst(new Idivq(dest));

            // Move contents of %rax to destination.
            x86.addInst(new Movq(new Register(Registers.RAX), dest));
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
                x86.addInst(new Movq(new Temporary(param), new Register(Registers.RDI)));
                x86.addInst(new Callq(new Global("print_int")));
                if (procName.equals("println"))
                    x86.addInst(new Callq(new Global("print_nl")));
            }
        }
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
            return new Immediate(String.format("$%s", addr));
        else if (addr instanceof TempAddress)
            return new Temporary(addr.toString());
        else
            throw new Error("transAddress(Address): Cannot translate.");
    }

    private int closestMultiple(int n, int x) 
    {    
        if (x > n) 
           return x;

        n = n + x / 2; 
        n = n - (n % x); 
        return n;
    } 
}
