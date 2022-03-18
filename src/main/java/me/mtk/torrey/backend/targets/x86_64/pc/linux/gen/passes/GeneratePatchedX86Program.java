package me.mtk.torrey.backend.targets.x86_64.pc.linux.gen.passes;

import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.*;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.gen.X86Program;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions.*;

public final class GeneratePatchedX86Program implements Pass<X86Program>
{
  private X86Program x86;

  public GeneratePatchedX86Program(X86Program x86Program)
  {
    this.x86 = x86Program;
  }

  public X86Program pass()
  {
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
}
