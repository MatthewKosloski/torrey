package me.mtk.torrey.frontend.ir.addressing;

/**
 * Represents an address with CONSTANT addressing mode.
 */
public final class IRConstAddress extends IRIntegralAddress
{
  public IRConstAddress(long constant)
  {
    super(constant);
  }

  public IRConstAddress(boolean constant)
  {
    super(constant ? 1 : 0);
  }

  public static IRConstAddress from(IRNullAddress addr)
  {
    return new IRConstAddress(addr.value());
  }
}
