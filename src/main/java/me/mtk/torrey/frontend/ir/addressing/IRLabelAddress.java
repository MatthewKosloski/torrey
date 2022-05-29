package me.mtk.torrey.frontend.ir.addressing;

/**
 * Represents an address with LABEL addressing mode.
 */
public final class IRLabelAddress extends IRStringAddress
{
  public IRLabelAddress(String label)
  {
    super(IRAddressingMode.LABEL, label);
  }
}
