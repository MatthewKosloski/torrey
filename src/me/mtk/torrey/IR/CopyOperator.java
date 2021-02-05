package me.mtk.torrey.IR;

/**
 * The copy operator for IR instructions.
 */
public enum CopyOperator implements Operator
{
    ASSIGN ("=");
        
    private final String opText;

    CopyOperator(String opText)
    {
        this.opText = opText;
    }

    public String opText()
    {
        return opText;
    }
}