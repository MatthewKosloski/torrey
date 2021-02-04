package me.mtk.torrey.IR;

/**
 * Represents an address with CONSTANT addressing mode.
 */
public final class ConstAddress extends Address
{
    public ConstAddress(int constant)
    {
        super(AddressingMode.CONSTANT, String.valueOf(constant));
    }
}
