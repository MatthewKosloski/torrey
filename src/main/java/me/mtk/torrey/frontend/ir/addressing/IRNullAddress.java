package me.mtk.torrey.frontend.ir.addressing;

/**
 * An address that has constant addressing mode and indicates
 * that an expression does not evaluate to anything
 */
public class IRNullAddress extends IRIntegralAddress
{
  private static final IRNullAddress INSTANCE = new IRNullAddress();

  private IRNullAddress()
  {
    super(0);
  }

  @Override
  public IRNullAddress makeCopy()
  {
    return new IRNullAddress();
  }

  public static IRNullAddress getInstance()
  {
    return INSTANCE;
  }
}
