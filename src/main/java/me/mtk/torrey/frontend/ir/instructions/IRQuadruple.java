package me.mtk.torrey.frontend.ir.instructions;

import me.mtk.torrey.frontend.ir.addressing.IRAddress;
import me.mtk.torrey.frontend.ir.addressing.IRLabelAddress;
import me.mtk.torrey.frontend.ir.addressing.IRTempAddress;

/**
 * Represents an intermediate language instruction.
 */
public abstract class IRQuadruple 
{
    // The operator type of the instruction.
    protected IROpType opType;

    // The first argument of the instruction.
    protected IRAddress arg1;

    // The second argument of the instruction.
    protected IRAddress arg2;
    
    // The address at which the result of the instruction 
    // is to be stored.
    protected IRAddress result;
    
    /**
     * Instantiates a new quadruple to hold the properties
     * of an intermediate language instruction.
     * 
     * @param opType The operator type of the instruction.
     * @param arg1 The address at which the first argument is located.
     * @param arg2 The address at which the second argument is located.
     * @param result A temporary address to store the result of the
     * instruction.
     */
    public IRQuadruple(IROpType opType, IRAddress arg1, IRAddress arg2, IRTempAddress result)
    {
        this.opType = opType;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.result = result;
    }

    public IRQuadruple(IROpType opType, IRAddress arg1, IRAddress arg2, IRLabelAddress result)
    {
        this.opType = opType;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.result = result;
    }

    public IRQuadruple(IROpType opType, IRAddress arg)
    {
        this.opType = opType;
        this.arg1 = arg;
        this.arg2 = null;
        this.result = null;
    }

    public IROpType opType()
    {
        return opType;
    }

    public IRAddress arg1()
    {
        return arg1;
    }

    public IRAddress arg2()
    {
        return arg2;
    }

    public IRAddress result()
    {
        return result;
    }
}
