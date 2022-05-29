package me.mtk.torrey.frontend.ir.addressing;

/**
 * Represents an address with TEMP addressing mode.
 */
public final class IRTempAddress extends IRStringAddress
{
  public IRTempAddress(String value)
  {
    super(IRAddressingMode.TEMP, value);
  }
}
