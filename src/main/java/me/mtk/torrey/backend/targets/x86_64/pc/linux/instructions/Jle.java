package me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions;

import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.LabelAddress;

public final class Jle extends X86Inst
{
    public Jle(LabelAddress label)
    {
        super(X86OpType.JLE, label, null);
    }
}