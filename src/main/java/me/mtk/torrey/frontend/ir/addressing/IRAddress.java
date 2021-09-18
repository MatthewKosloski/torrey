package me.mtk.torrey.frontend.ir.addressing;

/**
 * Represents a virtual addressing mode. An addressing mode
 * is an expression that calculates an address in memory
 * to be read/written to. 
 * 
 * An address can be a constant, or a 
 * compiler-generated temporary.
 */
public abstract class IRAddress
{
    // The mode of this address.
    private IRAddressingMode mode;

    // The value of the address.
    private Object value;

    /**
     * Constructs a new address.
     * @param value The value stored at this address.
     */
    public IRAddress(IRAddressingMode mode, Object value)
    {
        this.mode = mode;
        this.value = value;
    }

    /**
     * Returns the addressing mode of this instruction.
     * 
     * @return An addressing mode.
     */
    public IRAddressingMode mode()
    {
        return mode;
    }

    /**
     * Return the value stored at this address.
     * 
     * @return A value.
     */
    public Object value()
    {
        return value;
    }

    /**
     * The string representation of this address.
     * 
     * @return A string containing the value of this address.
     */
    public String toString()
    {
        return String.format("%s", value.toString());
    }
}
