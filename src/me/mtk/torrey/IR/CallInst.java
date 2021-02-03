package me.mtk.torrey.IR;

/**
 * Represents a call IR instruction. This should come
 * after 0 or more parameter IR instructions.
 */
public final class CallInst extends IRInst
{
    // The name of the procedure that is to be called.
    private String name;

    // The number of parameters supplied to this call.
    private int params;

    /**
     * Instantiates a new call IR instruction.
     * 
     * @param procName The name of the procedure that is to
     * be called.
     * @param numParams The number of parameters that are
     * supplied to this call.
     */
    public CallInst(String procName, int numParams)
    {
        super(null);
        name = procName;
        params = numParams;
    }

    /**
     * The string representation of this call instruction.
     * 
     * @return The string of this instruction.
     */
    public String toString()
    {
        return String.format("call %s, %d", name, params);
    }
}
