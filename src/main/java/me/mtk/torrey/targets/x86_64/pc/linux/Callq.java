package me.mtk.torrey.targets.x86_64.pc.linux;

public final class Callq extends X86Inst 
{
    public Callq(Global procedure)
    {
        super("callq", procedure, null);
    }
}
