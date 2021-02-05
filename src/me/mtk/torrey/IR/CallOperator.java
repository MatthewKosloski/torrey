package me.mtk.torrey.IR;

/**
 * The call operator for IR instructions.
 */
public enum CallOperator implements Operator
{
    CALL ("call");
        
    private final String opText;

    CallOperator(String opText)
    {
        this.opText = opText;
    }

    public String opText()
    {
        return opText;
    }
}