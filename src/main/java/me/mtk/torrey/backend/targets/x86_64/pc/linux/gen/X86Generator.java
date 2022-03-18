package me.mtk.torrey.backend.targets.x86_64.pc.linux.gen;

import me.mtk.torrey.backend.targets.x86_64.pc.linux.gen.passes.GeneratePatchedX86Program;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.gen.passes.GeneratePseudoX86Program;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.gen.passes.GenerateRunnableX86Program;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.gen.passes.GenerateX86Program;
import me.mtk.torrey.frontend.ir.gen.IRProgram;
/**
 * Generates 64-bit x86 assembly code
 * from three-address code represented by
 * a collection of quadruples of the form
 * (op, arg1, arg2, result).
 */
public final class X86Generator
{
  public static X86Program buildX86ProgramFromIRProgram(IRProgram irProgram)
  {
    // Performs the following four compiler passes:
    //
    //
    //
    // 1. Generates a psuedo-x86 program from the IR program. It's "pseudo"
    //    x86 because the output program is a sequence of x86 instructions
    //    that operate on virtual registers.
    //
    //   Example output:
    //
    //   movq   $2, %r10
    //   movq   $2, %r11
    //   cmp    %r11, %r10
    //   jne    l0
    //   movq   $5, t0 <--- virtual register
    //
    final GeneratePseudoX86Program pseudoX86ProgramGen =
      new GeneratePseudoX86Program(irProgram);
    final X86Program pseudoX86 = pseudoX86ProgramGen.pass();
    //
    //
    //
    // 2. Replaces the virtual registers with base-relative stack locations.
    //    In the future, we (hopefully) will be using register allocation.
    //
    //   Example output:
    //
    //   movq   $2, %r10
    //   movq   $2, %r11
    //   cmp    %r11, %r10
    //   jne    l0
    //   movq   $5, -8(%rbp) <--- base-relative stack location
    //
    final GenerateX86Program x86ProgramGen = new GenerateX86Program(pseudoX86);
    final X86Program x86 = x86ProgramGen.pass();
    //
    //
    //
    // 3. Ensures that each x86 instruction adheres to the restriction that
    //    at most one operand of an instruction may be a memory reference.
    //    This is accomplished by moving one of the memory locations to
    //    a register.
    //
    //    Example
    //    movq -8(%rbp), -16(%rbp)    ->    movq -8(%rbp), %r10
    //                                      movq %r10, -16(%rbp)
    final GeneratePatchedX86Program patchedX86ProgramGen =
      new GeneratePatchedX86Program(x86);
    final X86Program patchedX86 = patchedX86ProgramGen.pass();
    //
    //
    //
    // 4. Adds a prelude and conclusion to the program, allowing it to be run
    //    by the operating system. Additionally, the instructions added to the
    //    program in in this pass build the stack.
    //
    //
    final GenerateRunnableX86Program runnableX86ProgramGen =
      new GenerateRunnableX86Program(patchedX86);
    final X86Program runnableX86 = runnableX86ProgramGen.pass();

    return runnableX86;
  }
}
