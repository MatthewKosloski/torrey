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
    public IntegralIRAddress(long value)
    {
      this.mode = IRAddressingMode.CONSTANT;
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

    @Override
    public boolean equals(Object o)
    {
      if (o == this)
      {
        return true;
      }

      if (!(o instanceof IntegralIRAddress))
      {
        return false;
      }

      IntegralIRAddress that = (IntegralIRAddress)o;

      return this.mode == that.mode && this.value == that.value;
    }

    @Override
    public int hashCode()
    {
      int result = this.mode.hashCode();
      result = 31 * result + Long.hashCode(value);
      return result;
    }

    /**
     * The string representation of this address.
     *
     * @return A string containing the value of this address.
     */
    @Override
    public String toString()
    {
      return Long.toString(value);
    }
}
