package me.mtk.torrey.IR;

/**
 * Represents a binary arithmetic IR instruction.
 */
public class BinaryInst extends IRInst
{
    // The type of binary operator.
    BinaryOpType operator;

    // The first and second operands.
    Address first, second;

    /**
     * Instantiates a new binary arithmetic IR instruction.
     * 
     * @param addr The address at which the result of the arithmetic is to be stored.
     * @param op The binary operator of this instruction.
     * @param first The address at which the first operand is located.
     * @param second The address at which the second operand is located.
     */
    public BinaryInst(Address addr, BinaryOpType op, Address first, Address second)
    {
        super(addr);
        operator = op;
        this.first = first;
        this.second = second;
    }

    /**
     * The string representation of this binary arithmetic instruction.
     * 
     * @return The string of this instruction.
     */
    public String toString()
    {
        return String.format("%s = %s %s %s", addr, operator, first, second);
    }
}
