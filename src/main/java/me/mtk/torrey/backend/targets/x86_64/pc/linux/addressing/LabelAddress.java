package me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing;

public final class LabelAddress extends X86Address
{
  public LabelAddress(String labelName)
  {
    super(AddressingMode.LABEL, labelName);
  }
}
