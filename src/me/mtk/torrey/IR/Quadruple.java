package me.mtk.torrey.IR;

/**
 * Represents an intermediate language instruction.
 */
public class Quadruple 
{
    // The operator of the instruction.
    protected Operator op;

    // The first argument of the instruction.
    protected Address arg1;

    // The second argument of the instruction.
    protected Address arg2;
    
    // The address at which the result of the instruction 
    // is to be stored.
    protected Address result;
    
    /**
     * Instantiates a new quadruple to hold the properties
     * of an intermediate language instruction.
     * 
     * @param op The operator of the instruction.
     * @param arg1 The address at which the first argument is located.
     * @param arg2 The address at which the second argument is located.
     * @param result A temporary address to store the result of the
     * instruction. The addressing mode must be TEMP.
     */
    public Quadruple(Operator op, Address arg1, Address arg2, Address result)
    {
        if (result != null && result.mode() != AddressingMode.TEMP)
            throw new Error("The result must be a temporary register.");

        this.op = op;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.result = result;
    }
}
