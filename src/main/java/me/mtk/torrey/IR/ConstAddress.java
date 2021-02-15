package me.mtk.torrey.ir;

/**
 * Represents an address with CONSTANT addressing mode.
 */
public final class ConstAddress extends Address
{
    public ConstAddress(int constant)
    {
        super(AddressingMode.CONSTANT, constant);
    }

    public ConstAddress(String constantStr)
    {
        super(AddressingMode.CONSTANT, Integer.parseInt(constantStr));
    }
}
