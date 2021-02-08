package me.mtk.torrey.IR;

/**
 * Represents a virtual addressing mode. An addressing mode
 * is an expression that calculates an address in memory
 * to be read/written to. 
 * 
 * An address can be a constant, or a 
 * compiler-generated temporary.
 */
public class Address
{
    // The mode of this address.
    protected AddressingMode mode;

    // The value of the address.
    protected Object value;

    /**
     * Constructs a new address.
     * @param value The value stored at this address.
     */
    public Address(AddressingMode mode, Object value)
    {
        this.mode = mode;
        this.value = value;
    }

    /**
     * Returns the addressing mode of this instruction.
     * 
     * @return An addressing mode.
     */
    public AddressingMode mode()
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
