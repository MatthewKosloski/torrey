package me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions;

import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.X86Address;

public final class Movabsq extends X86Inst
{
  public Movabsq(X86Address src, X86Address dest)
  {
    super(OpType.MOVABSQ, src, dest);
  }
}
