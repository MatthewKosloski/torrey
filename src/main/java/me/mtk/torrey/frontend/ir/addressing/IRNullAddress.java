package me.mtk.torrey.frontend.ir.addressing;

/**
 * An address that has constant addressing mode and indicates
 * that an expression does not evaluate to anything
 */
public final class IRNullAddress extends IRAddress
{
  public IRNullAddress()
  {
    super(IRAddressingMode.CONSTANT, 0);
  }

  public IRAddress toIRConstantAddress()
  {
    return new IRConstAddress(0);
  }
}
