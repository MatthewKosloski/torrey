package me.mtk.torrey.IR;

/**
 * Represents a binary IR instruction.
 */
public class BinaryInst extends Quadruple
{
    /**
     * Instantiates a new binary IR instruction.
     * 
     * @param op The binary operator of this instruction.
     * @param arg1 The address at which the first operand is located.
     * @param arg2 The address at which the second operand is located.
     * @param result The address at which the result of the operation is to be stored.
     */
    public BinaryInst(BinaryOperator op, Address arg1, Address arg2, Address result)
    {
        super(op, arg1, arg2, result);
    }

    /**
     * Instantiates a new binary IR instruction 
     * with no result address.
     * 
     * @param op The binary operator of this instruction.
     * @param arg1 The address at which the first operand is located.
     * @param arg2 The address at which the second operand is located.
     */
    public BinaryInst(BinaryOperator op, Address arg1, Address arg2)
    {
        this(op, arg1, arg2, null);
    }

    /**
     * The string representation of this binary instruction.
     * 
     * @return The string of this instruction.
     */
    public String toString()
    {
        return String.format("%s = %s %s %s", result, arg1, op.opText(), arg2);
    }
}
