package me.mtk.torrey.IR;

/**
 * Represents an integer IR instruction.
 */
public class IntegerInst extends IRInst
{
    // A constant address to hold the integer.
    private Address constant;

    /**
     * Instantiates a new integer IR instruction.
     * 
     * @param addr The address at which the integer constant is to be stored.
     * @param constant The actual integer constant value.
     */
    public IntegerInst(Address addr, int constant)
    {
        super(addr);
        this.constant = new Address(constant);
    }

    /**
     * The string representation of this binary arithmetic instruction.
     * 
     * @return The string of this instruction.
     */
    public String toString()
    {
        return String.format("%s = %s", addr, constant);
    }
}
