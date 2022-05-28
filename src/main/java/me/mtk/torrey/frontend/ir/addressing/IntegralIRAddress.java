package me.mtk.torrey.frontend.ir.addressing;

public abstract class IntegralIRAddress implements IRAddress
{
    // The mode of this address.
    private IRAddressingMode mode;

    // The integral value of the address.
    private long value;

    /**
     * Constructs a new address.
     * @param value The integral value stored at this address.
     */
    public IntegralIRAddress(IRAddressingMode mode, long value)
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
    public Long value()
    {
      return value;
    }

    /**
     * The string representation of this address.
     *
     * @return A string containing the value of this address.
     */
    @Override
    public String toString()
    {
      return String.format("%s", Long.toString(value));
    }
}
