package me.mtk.torrey.IR;

/**
 * Represents a parameter IR instruction.
 */
public class ParamInst extends IRInst
{
    /**
     * Instantiates a new parameter instruction.
     * 
     * @param addr The address at which the value of the parameter is located.
     */
    public ParamInst(Address addr)
    {
        super(addr);
    }

    /**
     * The string representation of this binary arithmetic instruction.
     * 
     * @return The string of this instruction.
     */
    public String toString()
    {
        return String.format("param %s", addr);
    }
}
