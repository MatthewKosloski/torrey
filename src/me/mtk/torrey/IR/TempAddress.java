package me.mtk.torrey.IR;

/**
 * Represents an address with TEMP addressing mode.
 */
public final class TempAddress extends Address
{
    public TempAddress(String tempName)
    {
        super(AddressingMode.TEMP, tempName);
    }
}
