package me.mtk.torrey.IR;

/**
 * To be implemented by all intermediate language
 * instruction operators.
 */
public interface Operator
{
    /**
     * Returns the operator text that will appear in the 
     * intermediate instruction.
     * 
     * @return The operator text.
     */
    public String opText();
}