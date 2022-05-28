package me.mtk.torrey.frontend.ir.addressing;

/**
 * An address that has constant addressing mode and indicates
 * that an expression does not evaluate to anything
 */
public final class IRNullAddress extends IntegralIRAddress
{
  public IRNullAddress()
  {
    super(0);
  }

  public IRConstAddress toIRConstantAddress()
  {
    return new IRConstAddress(0);
  }
}
