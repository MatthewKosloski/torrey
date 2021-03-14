package me.mtk.torrey.targets.x86_64.pc.linux;

public final class Negq extends X86Inst 
{
    public Negq(X86Address dest)
    {
        super("negq", dest, null);
    }    
}
