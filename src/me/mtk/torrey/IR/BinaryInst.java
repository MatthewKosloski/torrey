package me.mtk.torrey.IR;

public class BinaryInst extends IRInst
{
    // the binary operator
    BinaryOpType operator;

    // left and right operands
    Address left, right;

    public BinaryInst(Address addr, BinaryOpType op, Address left, Address right)
    {
        super(addr);
        operator = op;
        this.left = left;
        this.right = right;
    }

    public String toString()
    {
        return String.format("%s = %s %s %s", addr, operator, left, right);
    }
}
