package me.mtk.torrey.targets.x86_64.pc.linux;

public final class Movq extends X86Inst 
{
    public Movq(X86Address src, X86Address dest)
    {
        super("movq", src, dest);
    }    
}
