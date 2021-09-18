package me.mtk.torrey.frontend.ir.instructions;

import me.mtk.torrey.frontend.ir.addressing.IRAddress;

/**
 * Represents a parameter IR instruction.
 */
public final class IRParamInst extends IRQuadruple
{
    /**
     * Instantiates a new parameter instruction.
     * 
     * @param addr The address at which the value of the parameter is located.
     */
    public IRParamInst(IRAddress addr)
    {
        super(new IRParamOperator(), addr);
    }

    /**
     * The string representation of this binary arithmetic instruction.
     * 
     * @return The string of this instruction.
     */
    public String toString()
    {
        return String.format("%s %s", op.opText(), arg1);
    }
}
