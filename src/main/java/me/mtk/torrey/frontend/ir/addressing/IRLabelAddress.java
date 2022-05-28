package me.mtk.torrey.frontend.ir.addressing;

/**
 * Represents an address with LABEL addressing mode.
 */
public final class IRLabelAddress extends StringIRAddress
{
  // The number of the label address.
  private static int num = 0;

  public IRLabelAddress()
  {
    super(IRAddressingMode.LABEL, String.format("l%d", num++));
  }
}
