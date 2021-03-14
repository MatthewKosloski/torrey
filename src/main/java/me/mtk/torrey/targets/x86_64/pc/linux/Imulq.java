package me.mtk.torrey.targets.x86_64.pc.linux;

public final class Imulq extends X86Inst
{
    public Imulq(Register srcDest)
    {
        super("imulq", srcDest, null);
    }    
}
