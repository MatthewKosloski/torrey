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

    public ConstAddress(String constant)
    {
        super(AddressingMode.CONSTANT, constant);
    }

    /**
     * Returns the value stored at this constant address
     * as an integer.
     * 
     * @return The integer stored at this address.
     */
    public int getConstant()
    {
        return Integer.parseInt(value);
    }
}
