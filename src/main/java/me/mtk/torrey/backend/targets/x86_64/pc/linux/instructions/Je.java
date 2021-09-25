package me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions;

import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.LabelAddress;

public final class Je extends X86Inst
{
    public Je(LabelAddress label)
    {
        super(OpType.JE, label, null);
    }
}