package me.mtk.torrey.IR;

/**
 * The param operator for IR instructions.
 */
public enum ParamOperator implements Operator
{
    PARAM ("param");
        
    private final String opText;

    ParamOperator(String opText)
    {
        this.opText = opText;
    }

    public String opText()
    {
        return opText;
    }
}