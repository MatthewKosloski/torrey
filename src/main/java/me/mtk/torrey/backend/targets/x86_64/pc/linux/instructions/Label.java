package me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions;

import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.LabelAddress;

public final class Label extends X86Inst
{
  public Label(LabelAddress label)
  {
    super(OpType.LABEL, label, null);
  }

  public String toString()
  {
    return String.format("%s:", arg1().toString());
  }
}
