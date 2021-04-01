package me.mtk.torrey.frontend.ir.addressing;

/**
 * Represents an address with LABEL addressing mode.
 */
public final class LabelAddress extends Address
{
    // The number of the label address.
    private static int num = 0;

    public LabelAddress()
    {
        super(AddressingMode.LABEL, String.format("l%d", num++));
    }
}
