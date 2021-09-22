package me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions;

import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.X86Address;

public final class Addq extends X86Inst
{
    public Addq(X86Address src, X86Address dest)
    {
        super(X86OpType.ADDQ, src, dest);
    }  
}
