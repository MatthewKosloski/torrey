package me.mtk.torrey.IR;

public class IntegerInst extends IRInst
{
    // The literal value of the integer.
    private int value;

    public IntegerInst(Address addr, int value)
    {
        super(addr);
        this.value = value;
    }

    public String toString()
    {
        return String.format("%s = %d", addr, value);
    }
}
