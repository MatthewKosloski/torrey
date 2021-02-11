package me.mtk.torrey.IR;

/**
 * Represents an address with NAME addressing mode.
 */
public final class NameAddress extends Address
{
    public NameAddress(String name)
    {
        super(AddressingMode.NAME, name);
    }
}
