package me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions;

import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.X86Address;

public final class Negq extends X86Inst
{
  public Negq(X86Address dest)
  {
    super(OpType.NEGQ, dest, null);
  }
}
