package me.mtk.torrey.frontend.ir.instructions;

import me.mtk.torrey.frontend.ir.addressing.IRAddress;
import me.mtk.torrey.frontend.ir.addressing.IRTempAddress;

/**
 * Represents a binary IR instruction.
 */
public class IRBinaryInst extends IRQuadruple
{
    /**
     * Instantiates a new binary IR instruction.
     * 
     * @param op The binary operator type of this instruction.
     * @param arg1 The address at which the first operand is located.
     * @param arg2 The address at which the second operand is located.
     * @param result The temp address at which the result of the operation 
     * is to be stored. 
     */
    public IRBinaryInst(IRBinaryOpType op, IRAddress arg1, IRAddress arg2, 
        IRTempAddress result)
    {
        super(new IRBinaryOperator(op), arg1, arg2, result);
    }

    /**
     * Instantiates a new binary IR instruction 
     * with no result address.
     * 
     * @param op The binary operator type of this instruction.
     * @param arg1 The address at which the first operand is located.
     * @param arg2 The address at which the second operand is located.
     */
    public IRBinaryInst(IRBinaryOpType op, IRAddress arg1, IRAddress arg2)
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
