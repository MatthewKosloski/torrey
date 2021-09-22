package me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions;

import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.X86Address;

public final class Movq extends X86Inst 
{
    public Movq(X86Address src, X86Address dest)
    {
        super(X86OpType.MOVQ, src, dest);
    }    
}
