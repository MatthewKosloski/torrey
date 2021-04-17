package me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions;

import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.LabelAddress;

public final class Jcc extends X86Inst
{
    public Jcc(ConditionCode cc, LabelAddress label)
    {
        super(cc.toString().toLowerCase(), label, null);
    }
}