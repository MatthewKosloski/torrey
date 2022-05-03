package me.mtk.torrey.frontend.ir.addressing;

/**
 * Represents an address with CONSTANT addressing mode.
 */
public final class IRConstAddress extends IRAddress
{
  public IRConstAddress(long constant)
  {
    super(IRAddressingMode.CONSTANT, constant);
  }

  public IRConstAddress(boolean constant)
  {
    super(IRAddressingMode.CONSTANT, constant ? 1 : 0);
  }

  public IRConstAddress(String constantStr)
  {
    super(IRAddressingMode.CONSTANT, Long.parseLong(constantStr));
  }
}
