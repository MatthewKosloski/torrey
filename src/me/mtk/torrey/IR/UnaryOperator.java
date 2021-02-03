package me.mtk.torrey.IR;

/**
 * The types of unary operators for IR instructions.
 */
public enum UnaryOperator implements Operator
{
    MINUS ("-"),
    PARAM ("param"),
    ASSIGN ("=");
        
    private final String opText;

    UnaryOperator(String opText)
    {
        this.opText = opText;
    }

    public String opText()
    {
        return opText;
    }
}