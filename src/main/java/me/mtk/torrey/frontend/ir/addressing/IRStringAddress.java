package me.mtk.torrey.frontend.ir.addressing;

public abstract class IRStringAddress implements IRAddress
{
    // The mode of this address.
    private IRAddressingMode mode;

    // The string value of the address.
    private String value;

    /**
     * Constructs a new address.
     * @param value The string value stored at this address.
     */
    public IRStringAddress(IRAddressingMode mode, String value)
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
    public String value()
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

      if (!(o instanceof IRStringAddress))
      {
        return false;
      }

      IRStringAddress that = (IRStringAddress)o;

      return this.mode == that.mode && this.value.equals(that.value);
    }

    @Override
    public int hashCode()
    {
      int result = this.mode.hashCode();
      result = 31 * result + this.value.hashCode();
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
      return value;
    }
}
