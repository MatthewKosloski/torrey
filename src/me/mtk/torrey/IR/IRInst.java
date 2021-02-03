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
            throwIllegalAddressingModeException(AddressingMode.TEMP, lval.mode());

        this.lval = lval;
    }

    /**
     * Should be thrown when we encounter an unexpected addressing mode.
     * 
     * @param expected The expected addressing mode.
     * @param actual The actual addressing mode.
     */
    protected void throwIllegalAddressingModeException(AddressingMode expected,
        AddressingMode actual)
    {
        final String msg = String.format("Expected addessing mode '%s' but" +
            " got addessing mode '%s'", expected, actual);
        throw new IllegalAddressingModeException(msg);
    }
}
