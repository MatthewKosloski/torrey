package me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions;

import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.X86Address;

public final class Idivq extends X86Inst
{
    public Idivq(X86Address dest)
    {
        super(X86OpType.IDIVQ, dest, null);
    }    
}
