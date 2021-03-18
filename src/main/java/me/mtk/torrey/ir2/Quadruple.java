package me.mtk.torrey.ir;

/**
 * Represents an intermediate language instruction.
 */
public abstract class Quadruple 
{
    // The operator of the instruction.
    protected Operator op;

    // The first argument of the instruction.
    protected Address arg1;

    // The second argument of the instruction.
    protected Address arg2;
    
    // The address at which the result of the instruction 
    // is to be stored.
    protected TempAddress result;
    
    /**
     * Instantiates a new quadruple to hold the properties
     * of an intermediate language instruction.
     * 
     * @param op The operator of the instruction.
     * @param arg1 The address at which the first argument is located.
     * @param arg2 The address at which the second argument is located.
     * @param result A temporary address to store the result of the
     * instruction.
     */
    public Quadruple(Operator op, Address arg1, Address arg2, TempAddress result)
    {
        this.op = op;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.result = result;
    }

    public Quadruple(Operator op, Address arg)
    {
        this.op = op;
        this.arg1 = arg;
        this.arg2 = null;
        this.result = null;
    }

    public Operator op()
    {
        return op;
    }

    public Address arg1()
    {
        return arg1;
    }

    public Address arg2()
    {
        return arg2;
    }

    public TempAddress result()
    {
        return result;
    }
}
