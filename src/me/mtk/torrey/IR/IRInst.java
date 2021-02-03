package me.mtk.torrey.IR;

/**
 * The base class of all intermediate language instructions.
 */
public class IRInst 
{
    // The address on the left-hand side of the instruction. That is,
    // the address at which the value of the instruction is to be stored.
    protected Address lval;

    public IRInst(Address lval)
    {
        this.lval = lval;
    }
}
