package me.mtk.torrey.frontend.ir.addressing;

/**
 * Represents an address with CONSTANT addressing mode.
 */
public final class IRConstAddress extends IRAddress
{
  public IRConstAddress(int constant)
  {
    super(IRAddressingMode.CONSTANT, constant);
  }

  public IRConstAddress(boolean constant)
  {
    super(IRAddressingMode.CONSTANT, constant ? 1 : 0);
  }

  public IRConstAddress(String constantStr)
  {
    super(IRAddressingMode.CONSTANT, Integer.parseInt(constantStr));
  }
}
