package me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions;

import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.LabelAddress;

public final class Jl extends X86Inst
{
    public Jl(LabelAddress label)
    {
        super(X86OpType.JL, label, null);
    }
}