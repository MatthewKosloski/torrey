package me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions;

import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.LabelAddress;

public final class Jge extends X86Inst
{
  public Jge(LabelAddress label)
  {
    super(OpType.JGE, label, null);
  }
}
