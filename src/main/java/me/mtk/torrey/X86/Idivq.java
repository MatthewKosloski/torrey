package me.mtk.torrey.x86;

public final class Idivq extends X86Inst
{
    public Idivq(X86Address dest)
    {
        super("idivq", dest, null);
    }    
}
