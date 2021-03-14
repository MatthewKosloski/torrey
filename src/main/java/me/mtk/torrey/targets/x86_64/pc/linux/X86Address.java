package me.mtk.torrey.targets.x86_64.pc.linux;

/**
 * Represents an x86 argument to an instruction,
 * storing the value of the argument and its
 * addressing mode.
 */
public abstract class X86Address 
{
    // The argument's addressing mode.
    private AddressingMode mode;

    // The value of the address.
    private Object value;

    /**
     * Constructs a new address.
     * @param value The value stored at this address.
     */
    public X86Address(AddressingMode mode, Object value)
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
