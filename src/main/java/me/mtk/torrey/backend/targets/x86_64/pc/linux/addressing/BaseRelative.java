package me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing;

public final class BaseRelative extends X86Address
{
  public BaseRelative(String tempName)
  {
    super(AddressingMode.BASEREL, tempName);
  }
}
