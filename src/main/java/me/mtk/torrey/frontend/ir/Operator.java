package me.mtk.torrey.frontend.ir;

/**
 * To be extended by all intermediate language
 * instruction operators.
 */
public abstract class Operator
{
    private final String opText;

    Operator(String opText)
    {
        this.opText = opText;
    }

    public String opText()
    {
        return opText;
    }
}