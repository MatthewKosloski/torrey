package me.mtk.torrey.IR;

/**
 * The types of binary operators for IR instructions.
 */
public enum BinaryOperator implements Operator
{
    ADD ("+"),
    SUB ("-"),
    MULT ("*"),
    DIV ("/");

    private final String opText;

    BinaryOperator(String opText)
    {
        this.opText = opText;
    }

    public String opText()
    {
        return opText;
    }
}