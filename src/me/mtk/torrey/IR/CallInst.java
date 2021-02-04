package me.mtk.torrey.IR;

public final class CallInst extends BinaryInst
{
    /**
     * Instantiates a procedure call IR instruction,
     * storing the result of the procedure in the 
     * specified result address.
     *
     * @param result The temp address at which the result of the
     * procedure is to be stored.
     * @param procName A name address containing the name of
     * the procedure.
     * @param numParams A constant address indicating the number
     * of parameters to the procedure.
     */
    public CallInst(TempAddress result, NameAddress procName, 
        ConstAddress numParams)
    {
        super(BinaryOperator.CALL, procName, numParams, result);
    }

    /**
     * Instantiates a procedure call IR instruction
     * without a result address.
     *
     * @param procName An name address containing the name of
     * the procedure.
     * @param numParams A constant address indicating the number
     * of parameters to the procedure.
     */
    public CallInst(NameAddress procName, ConstAddress numParams)
    {
        super(BinaryOperator.CALL, procName, numParams);
    }

    /**
     * The string respresentation of the call instruction.
     */
    public String toString()
    {
        return String.format("call %s, %s", arg1, arg2);
    }
}