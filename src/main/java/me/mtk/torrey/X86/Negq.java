package me.mtk.torrey.x86;

public final class Negq extends X86Inst 
{
    public Negq(X86Address dest)
    {
        super("negq", dest, null);
    }    
}
