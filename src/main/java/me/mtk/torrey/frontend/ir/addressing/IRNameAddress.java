package me.mtk.torrey.frontend.ir.addressing;

/**
 * Represents an address with NAME addressing mode.
 */
public final class IRNameAddress extends IRStringAddress
{
  public IRNameAddress(String name)
  {
    super(IRAddressingMode.NAME, name);
  }

  @Override
  public IRNameAddress makeCopy()
  {
    return new IRNameAddress(this.value());
  }
}
