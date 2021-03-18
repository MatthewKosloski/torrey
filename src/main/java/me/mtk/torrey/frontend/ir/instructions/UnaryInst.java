package me.mtk.torrey.frontend.ir.instructions;

import me.mtk.torrey.frontend.ir.addressing.Address;
import me.mtk.torrey.frontend.ir.addressing.TempAddress;

/**
 * Represents a unary IR instruction.
 */
public class UnaryInst extends Quadruple
{
    /**
     * Instantiates a new unary IR instruction.
     * 
     * @param op The unary operator type of this instruction.
     * @param arg The address at which the operand is located.
     * @param result The temp address at which the result of the operation
     * is to be stored.
     */
    public UnaryInst(UnaryOpType op, Address arg, TempAddress result)
    {
        super(new UnaryOperator(op), arg, null, result);
    }

    /**
     * Instantiates a new unary IR instruction
     * with no result address.
     * 
     * @param op The unary operator type of this instruction.
     * @param arg The address at which the operand is located.
     */
    public UnaryInst(UnaryOpType op, Address arg)
    {
        this(op, arg, null);
    }

    /**
     * The string representation of this unary instruction.
     * 
     * @return The string of this instruction.
     */
    public String toString()
    {
        return String.format("%s = %s %s", result, op.opText(), arg1);
    }
}
