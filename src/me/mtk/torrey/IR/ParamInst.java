package me.mtk.torrey.IR;

public class ParamInst extends IRInst
{
    public ParamInst(Address addr)
    {
        super(addr);
    }

    public String toString()
    {
        return String.format("param %s", addr);
    }
}
