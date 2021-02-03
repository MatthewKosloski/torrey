package me.mtk.torrey.IR;

/**
 * Represents a parameter IR instruction.
 */
public final class ParamInst extends UnaryInst
{
    /**
     * Instantiates a new parameter instruction.
     * 
     * @param addr The address at which the value of the parameter is located.
     */
    public ParamInst(Address addr)
    {
        super(UnaryOperator.PARAM, addr);
    }

    /**
     * The string representation of this binary arithmetic instruction.
     * 
     * @return The string of this instruction.
     */
    public String toString()
    {
        return String.format("param %s", arg1);
    }
}
