package me.mtk.torrey.IR;

public class IntegerInst extends IRInst
{
    private Address constant;

    public IntegerInst(Address addr, int constant)
    {
        super(addr);
        this.constant = new Address(constant);
    }

    public String toString()
    {
        return String.format("%s = %s", addr, constant);
    }
}
