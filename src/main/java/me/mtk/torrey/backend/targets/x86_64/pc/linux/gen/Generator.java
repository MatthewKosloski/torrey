package me.mtk.torrey.backend.targets.x86_64.pc.linux.gen;

import java.util.*;
import me.mtk.torrey.frontend.ir.addressing.*;
import me.mtk.torrey.frontend.ir.gen.IRProgram;
import me.mtk.torrey.frontend.ir.instructions.*;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.*;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.Global.RuntimeProcedure;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.assembler.*;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions.*;
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

    // The program's exit code to be returned to the OS upon
    // program termination.
    private static final int EXIT_CODE = 0;
    
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
        // Add assembler directives to the text segment.
        x86.addTextSegmentDirective(new AssemblerDirective(
            AssemblerDirectiveType.GLOBL, 
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
        for (IRQuadruple quad : ir.quads())
        {
            if (quad instanceof IRCopyInst)
                gen((IRCopyInst) quad);
            else if (quad instanceof IRUnaryInst)
                gen((IRUnaryInst) quad);
            else if (quad instanceof IRBinaryInst)
                gen((IRBinaryInst) quad);
            else if (quad instanceof IRParamInst)
                gen((IRParamInst) quad);
            else if (quad instanceof IRCallInst)
                gen((IRCallInst) quad);
            else if (quad instanceof IRIfInst)
                gen((IRIfInst) quad);
            else if (quad instanceof IRLabelInst)
                gen((IRLabelInst) quad);
            else if (quad instanceof IRGotoInst)
                gen((IRGotoInst) quad);
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

            // Return a successfully exit code.
            .addInst(new Movq(new Immediate(EXIT_CODE), Register.RAX))

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

    private void gen(IRIfInst inst)
    {

        final LabelAddress labelAddr = new LabelAddress(inst.result().toString());

        if (inst.opType() != null)
        {
            // The test condition is a comparison expression.
            
            // Convert the IR operands to x86 addresses.
            final X86Address arg1 = transAddress(inst.arg1());
            final X86Address arg2 = transAddress(inst.arg2());
            
            // move the operands to temporary registers to
            // perform the comparison.
            x86.addInst(new Movq(arg1, Register.R10));
            x86.addInst(new Movq(arg2, Register.R11));

            // Perform the comparison.
            x86.addInst(new Cmp(Register.R11, Register.R10));
            x86.addInst(new Jcc(ConditionCode.transIrOp(inst.opType()), labelAddr));
        }
        else
        {
            // The test condition is a primitive boolean expression.
            final boolean bool = (Boolean) inst.arg1().value();      
            x86.addInst(new Movq(new Immediate(0), Register.R10));
            x86.addInst(new Movq(new Immediate(1), Register.R11));
            x86.addInst(new Cmp(Register.R11, Register.R10));
            x86.addInst(new Jcc(bool ? ConditionCode.JNE : ConditionCode.JE, 
                labelAddr));
        }
    }

    private void gen(IRLabelInst inst)
    {
        x86.addInst(new Label(new LabelAddress(inst.arg1().toString())));
    }

    private void gen(IRGotoInst inst)
    {
        x86.addInst(new Jmp(new LabelAddress(inst.result().toString())));
    }

    private void gen(IRCopyInst inst)
    {
        final IRAddress srcAddr = inst.arg1();
        final IRAddress destAddr = inst.result();

        final X86Address src = transAddress(srcAddr);
        final X86Address dest = new Temporary(destAddr.toString());

        x86.addInst(new Movq(src, dest));
    }

    private void gen(IRUnaryInst inst)
    { 
        final IRAddress srcAddr = inst.arg1();
        final IRAddress destAddr = inst.result();

        final X86Address src = transAddress(srcAddr);
        final X86Address dest = new Temporary(destAddr.toString());

        x86.addInst(new Movq(src, dest));
        x86.addInst(new Negq(dest));
    }

    private void gen(IRBinaryInst inst)
    {
        final String op = inst.opType().terminal();
        final IRAddress arg1Addr = inst.arg1();
        final IRAddress arg2Addr = inst.arg2();
        final IRAddress destAddr = inst.result();

        final X86Address arg1 = transAddress(arg1Addr);
        final X86Address arg2 = transAddress(arg2Addr);
        final Temporary dest = new Temporary(destAddr.toString());

        if (op.equals("+") || op.equals("-"))
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
        else if (op.equals("*"))
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
        else if (op.equals("/"))
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

    private void gen(IRParamInst inst)
    {
        params.add((String) inst.arg1().value());
    }

    private void gen(IRCallInst inst)
    {
        final String procName = (String) inst.arg1().value();
        final int numParams = (int) inst.arg2().value();

        if (procName.equals("print") || procName.equals("println"))
        {
            for (int i = 0; i < numParams; i++)
            {
                String param = params.remove();
                x86.addInst(new Movq(new Temporary(param), Register.RDI));
                x86.addInst(new Callq(new Global(RuntimeProcedure.PRINT_INT)));
                if (procName.equals("println"))
                    x86.addInst(new Callq(new Global(RuntimeProcedure.PRINT_NL)));
            }
        }
    }

    /*
     * Converts the given IR address to an equivalent x86 address.
     * 
     * @param addr An IR address.
     * @return An equivalent x86 address.
     */
    private X86Address transAddress(IRAddress addr)
    {
        if (addr instanceof IRConstAddress)
            return new Immediate(String.format("$%s", addr));
        else if (addr instanceof IRTempAddress)
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
