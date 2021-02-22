package me.mtk.torrey.ir;

/**
 * Represents a parameter IR instruction.
 */
public final class ParamInst extends Quadruple
{
    /**
     * Instantiates a new parameter instruction.
     * 
     * @param addr The address at which the value of the parameter is located.
     */
    public ParamInst(Address addr)
    {
        super(new ParamOperator(), addr);
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
