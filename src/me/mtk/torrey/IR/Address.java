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
    protected String value;

    /**
     * Constructs a new address.
     * @param value The value stored at this address.
     */
    public Address(AddressingMode mode, String value)
    {
        this.mode = mode;
        this.value = value;
    }

    /**
     * Constructs a new temporary address.
     * @param value The value stored at this address.
     */
    public Address(String value)
    {
        mode = AddressingMode.TEMP;
        this.value = value;
    }

    /**
     * Constructs a new constant address.
     * @param constant A constant to be stored at this address.
     */
    public Address(int contant)
    {
        mode = AddressingMode.CONSTANT;
        value = String.valueOf(contant);
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
     * The string representation of this address.
     * 
     * @return A string containing the value of this address.
     */
    public String toString()
    {
        return String.format("%s", value);
    }
}
