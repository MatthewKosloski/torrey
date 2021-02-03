package me.mtk.torrey.IR;

/**
 * Represents an unary IR instruction.
 */
public class UnaryInst extends IRInst
{
    // The type of unary operator.
    private String operator;

    // The address at which the operand is located.
    private Address operand;

    /**
     * Instantiates a new unary IR instruction.
     * 
     * @param lval The address at which the result of the unary 
     * operation is to be stored.
     * @param op The unary operator of this instruction.
     * @param operand The address at which the value of the operand is located.
     */
    public UnaryInst(Address lval, String op, Address operand)
    {
        super(lval);
        operator = op;
        this.operand = operand;

    }

    /**
     * The string representation of this binary arithmetic instruction.
     * 
     * @return The string of this instruction.
     */
    public String toString()
    {
        return String.format("%s = %s %s", lval, operator, operand);
    }
}
