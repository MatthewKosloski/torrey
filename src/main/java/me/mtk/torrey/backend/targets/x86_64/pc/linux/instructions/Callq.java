package me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions;

import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.Global;

public final class Callq extends X86Inst
{
  public Callq(Global procedure)
  {
    super(OpType.CALLQ, procedure, null);
  }
}
