package me.mtk.torrey.frontend.ir.addressing;

/**
 * Represents an address with NAME addressing mode.
 */
public final class IRNameAddress extends StringIRAddress
{
  public IRNameAddress(String name)
  {
    super(IRAddressingMode.NAME, name);
  }
}
