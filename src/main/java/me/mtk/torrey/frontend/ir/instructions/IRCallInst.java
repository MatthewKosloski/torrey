package me.mtk.torrey.frontend.ir.instructions;

import me.mtk.torrey.frontend.ir.addressing.IRTempAddress;
import me.mtk.torrey.frontend.ir.addressing.IRNameAddress;
import me.mtk.torrey.frontend.ir.addressing.IRConstAddress;

public final class IRCallInst extends IRQuadruple
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
    public IRCallInst(IRTempAddress result, IRNameAddress procName,
        IRConstAddress numParams)
    {
        super(new IRCallOperator(), procName, numParams, result);
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
    public IRCallInst(IRNameAddress procName, IRConstAddress numParams)
    {
        this(null, procName, numParams);
    }

    /**
     * The string respresentation of the call instruction.
     */
    public String toString()
    {
        return String.format("%s %s, %s", op.opText(), arg1, arg2);
    }
}