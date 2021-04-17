package me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions;

import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.Register;

/**
 * Represents an x86-64 comparison instruction. 
 * 
 * Compares the first source operand with the second source 
 * operand and sets the status flags in the EFLAGS register 
 * according to the results. This instruction is typically 
 * used in conjunction with a conditional jump (e.g., je, 
 * jne, jg, jge, jl, jle, etc.).
 * 
 * The syntax is rather confusing, as for example:
 *   cmp $0, %rax
 *   jl branch
 * 
 * The following code will conditionally branch to
 * label "branch" iff %rax < 0 is true (not the opposite
 * as one might expect).
 * 
 */
public final class Cmp extends X86Inst
{
    public Cmp(Register r1, Register r2)
    {
        super("cmp", r1, r2);
    }
}
