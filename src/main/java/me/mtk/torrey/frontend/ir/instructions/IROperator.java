package me.mtk.torrey.frontend.ir.instructions;

/**
 * To be extended by all intermediate language
 * instruction operators.
 */
public abstract class IROperator
{
    private final String opText;

    IROperator(String opText)
    {
        this.opText = opText;
    }

    public String opText()
    {
        return opText;
    }
}