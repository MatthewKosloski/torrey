package me.mtk.torrey.x86;

import java.util.ArrayList;
import java.util.List;

public final class X86Program 
{
    private List<X86Inst> instrs;

    private int stackSize;

    public X86Program(int stackSize)
    {
        // Ensure the stack pointer is 16-bytes aligned
        // by setting this.StackSize to the nearest
        // multiple of 16.
        if (stackSize % 16 != 0)
            this.stackSize = closestMultiple(stackSize, 16);
        else
            this.stackSize = stackSize;

        instrs = new ArrayList<>();
    }

    public void addInst(X86Inst inst)
    {
        instrs.add(inst);
    }

    public List<X86Inst> instrs()
    {
        return instrs;
    }

    // TODO: Do something better than this...
    public String toString()
    {
        final StringBuilder sb = new StringBuilder(".text\n");

        // Make the `main` procedure externally visible 
        // so the operating system can call it. The OS
        // will then be able to issue a `callq main`
        // instruction, which pushes its return address
        // on the top of our stack frame before jumping
        // to `main`.
        sb.append("\t.global main\n");
        
        // Populate the `start` procedure with the program instructions.
        // Conclude the procedure with an unconditional control flow
        // jump to the `conclusion` procedure.
        sb.append("start:\n");
        for (X86Inst inst : instrs)
            sb.append(String.format("\t%s\n", inst));
        sb.append("\tjmp conclusion\n");

        // The entry point of our program.
        sb.append("main:\n")
            // Save the caller's base pointer in our stack. The base
            // pointer points to the beginning of a stack frame. This
            // also aligns the stack pointer.
            .append("\tpushq %rbp\n")

            // The top of the stack now contains the caller's (the OS's)
            // base pointer (thus %rsp points to it). Change the base
            // pointer so that it points to the location of the old
            // base pointer.
            .append("\tmovq %rsp, %rbp\n")
            
            // Allocate the stack by moving the stack pointer down
            // stackSize bytes (remember, the stack grows downward,
            // so we must subtract to increase the stack size).
            .append("\tsubq $").append(stackSize).append(", %rsp\n")

            // Unconditionally jump to the start of our program.
            .append("\tjmp start\n");

        sb.append("conclusion:\n")
            // Move stack pointer up stackSize bytes to the old base pointer
            .append("\taddq $").append(stackSize).append(", %rsp\n")

            // Pop the old base pointer off the stack, storing it in
            // register %rbp
            .append("\tpopq %rbp\n")

            // Return a successfully exit code.
            .append("\tmovq $0, %rax\n")

            // Pop the OS's return address off the stack and jump to it.
            .append("\tretq\n");
    
        return sb.toString();
    }

    private int closestMultiple(int n, int x) 
    {    
        if(x>n) 
           return x; 
        n = n + x/2; 
        n = n - (n%x); 
        return n; 
    } 

}
