package me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions;

import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.LabelAddress;

public final class Jg extends X86Inst
{
    public Jg(LabelAddress label)
    {
        super(OpType.JG, label, null);
    }
}