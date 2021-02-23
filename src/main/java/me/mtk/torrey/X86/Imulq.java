package me.mtk.torrey.x86;

public final class Imulq extends X86Inst
{
    public Imulq(Register srcDest)
    {
        super("imulq", srcDest, null);
    }    
}
