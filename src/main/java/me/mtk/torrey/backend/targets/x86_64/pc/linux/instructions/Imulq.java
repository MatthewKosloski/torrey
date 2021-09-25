package me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions;

import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.Register;

public final class Imulq extends X86Inst
{
    public Imulq(Register srcDest)
    {
        super(OpType.IMULQ, srcDest, null);
    }    
}
