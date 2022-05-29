package me.mtk.torrey.frontend.ir.addressing;

/**
 * Represents an address with LABEL addressing mode.
 */
public final class IRLabelAddress extends StringIRAddress
{
  public IRLabelAddress(String label)
  {
    super(IRAddressingMode.LABEL, label);
  }
}
