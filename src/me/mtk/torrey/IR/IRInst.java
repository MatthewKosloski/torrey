package me.mtk.torrey.IR;

/**
 * The base class of all intermediate language instructions.
 */
public class IRInst 
{
    // The address on the left-hand side of the instruction. That is,
    // the address at which the value of the instruction is to be stored.
    protected Address lval;

    /**
     * Instantiates a new intermediate language instruction with
     * the given left-hand side.
     * 
     * @param lval
     */
    public IRInst(Address lval)
    {
        // Require the addressing mode to be a temporary. We make this
        // check so that the lval isn't something weird like a constant 
        // (e.g., 5 = t0).
        if (lval.mode() != AddressingMode.TEMP)
            throw new Error("The addressing mode of the lval must be TEMP.");

        this.lval = lval;
    }
}
