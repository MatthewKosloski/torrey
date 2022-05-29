package me.mtk.torrey.frontend.ir.addressing;

/**
 * Represents an address with TEMP addressing mode.
 */
public final class IRTempAddress extends StringIRAddress
{
  public IRTempAddress(String value)
  {
    super(IRAddressingMode.TEMP, value);
  }
}
