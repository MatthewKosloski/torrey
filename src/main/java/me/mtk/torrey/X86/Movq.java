package me.mtk.torrey.x86;

public final class Movq extends X86Inst 
{
    public Movq(X86Address src, X86Address dest)
    {
        super("movq", src, dest);
    }    
}
