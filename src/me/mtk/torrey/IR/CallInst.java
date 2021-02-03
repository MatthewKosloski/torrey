package me.mtk.torrey.IR;

public class CallInst extends IRInst 
{
    // the name of the procedure that is to be called
    private String name;

    // number or parameters supplied to this call
    private int params;

    public CallInst(Address addr, String procName, int numParams)
    {
        super(addr);
        name = procName;
        params = numParams;
    }

    public String toString()
    {
        return String.format("call %s, %d", name, params);
    }
}
