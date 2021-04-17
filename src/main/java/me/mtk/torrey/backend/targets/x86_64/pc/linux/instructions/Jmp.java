package me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions;

import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.LabelAddress;

public final class Jmp extends X86Inst
{
    public Jmp(LabelAddress label)
    {
        super("jmp", label, null);
    }
}