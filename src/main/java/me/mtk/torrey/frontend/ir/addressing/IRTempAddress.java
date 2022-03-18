package me.mtk.torrey.frontend.ir.addressing;

/**
 * Represents an address with TEMP addressing mode.
 */
public final class IRTempAddress extends IRAddress
{
  // The number of the temporary address.
  private static int num = 0;

  public IRTempAddress()
  {
    super(IRAddressingMode.TEMP, String.format("t%d", num++));
  }
}
