package me.mtk.torrey.x86;

public final class Callq extends X86Inst 
{
    public Callq(Global procedure)
    {
        super("callq", procedure, null);
    }
}
