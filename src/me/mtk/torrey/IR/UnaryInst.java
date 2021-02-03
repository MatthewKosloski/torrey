package me.mtk.torrey.IR;

public class UnaryInst extends IRInst
{
    // The type of unary operator.
    private UnaryOpType operator;

    private Address operand;

    public UnaryInst(Address addr, UnaryOpType op, Address operand)
    {
        super(addr);
        operator = op;
        this.operand = operand;

    }

    public String toString()
    {
        return String.format("%s = %s %s", addr, operator, operand);
    }
}
