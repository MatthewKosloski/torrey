package me.mtk.torrey.backend.targets.x86_64.pc.linux.gen.passes;

import me.mtk.torrey.backend.targets.x86_64.pc.linux.gen.X86Program;

import java.util.*;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.*;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.assembler.AssemblerDirective;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.assembler.AssemblerDirectiveType;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions.*;

public final class GenerateRunnableX86Program implements Pass<X86Program>
{
  private X86Program patchedX86Program;
  private X86Program runnableX86Program;

  // The name of the label of the program's prelude.
  private static final String PRELUDE_LABEL_NAME = "main";

  // The name of the label of the program's entry point.
  private static final String ENTRY_LABEL_NAME = "start";

  // The name of the label of the program's conclusion.
  private static final String CONCLUSION_LABEL_NAME = "conclusion";

  // The program's exit code to be returned to the OS upon
  // program termination.
  private static final int EXIT_CODE = 0;

  public GenerateRunnableX86Program(X86Program patchedX86Program)
  {
    this.patchedX86Program = patchedX86Program;
    this.runnableX86Program = new X86Program(patchedX86Program.stackSize());
  }

  public X86Program pass()
  {
    final LabelAddress preludeAddr = new LabelAddress(PRELUDE_LABEL_NAME);
    final LabelAddress entryAddr = new LabelAddress(ENTRY_LABEL_NAME);
    final LabelAddress concludeAddr = new LabelAddress(CONCLUSION_LABEL_NAME);

    final AssemblerDirective directive = new AssemblerDirective(
      AssemblerDirectiveType.GLOBL,
      Arrays.asList(PRELUDE_LABEL_NAME)
    );

    // Add assembler directives to the text segment.
    runnableX86Program.addTextSegmentDirective(directive)

      // Generate instructions for the program's entry point.
      .addInst(new Label(preludeAddr))

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
      .addInst(new Subq(new Immediate(runnableX86Program.stackSize()),
        Register.RSP))

      // Unconditionally jump to the start of our program.
      .addInst(new Jmp(entryAddr))

      // Emit the start label.
      .addInst(new Label(entryAddr));

      // Insert our program after the prelude and before the conclusion.
      for (X86Inst inst : patchedX86Program.instrs())
        runnableX86Program.addInst(inst);

      // Unconditionally jump to the conclusion of our program.
      runnableX86Program.addInst(new Jmp(concludeAddr))
        .addInst(new Label(concludeAddr))

        // Move the stack pointer up stackSize bytes to
        // the old base pointer.
        .addInst(new Addq(new Immediate(runnableX86Program.stackSize()),
          Register.RSP))

        // Pop the old base pointer off the stack, storing it in
        // register %rbp.
        .addInst(new Popq(Register.RBP))

        // Return a successfully exit code.
        .addInst(new Movq(new Immediate(EXIT_CODE), Register.RAX))

        // Pop the OS's return address off the stack and jump to it.
        .addInst(new Retq());

      return runnableX86Program;
  }
}
