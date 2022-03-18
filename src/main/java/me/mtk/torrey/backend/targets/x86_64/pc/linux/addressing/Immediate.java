package me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing;

public final class Immediate extends X86Address
{
  public Immediate(String immediate)
  {
    super(AddressingMode.IMMEDIATE, immediate);
  }

  public Immediate(int constant)
  {
    this(String.format("$%d", constant));
  }
}
