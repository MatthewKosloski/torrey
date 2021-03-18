package me.mtk.torrey.frontend.ir;

/**
 * Represents an address with TEMP addressing mode.
 */
public final class TempAddress extends Address
{
    // The number of the temporary address.
    private static int num = 0;

    public TempAddress()
    {
        super(AddressingMode.TEMP, String.format("t%d", num++));
    }
}
