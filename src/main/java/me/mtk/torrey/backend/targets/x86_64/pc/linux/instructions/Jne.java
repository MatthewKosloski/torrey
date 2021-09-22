package me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions;

import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.LabelAddress;

public final class Jne extends X86Inst
{
    public Jne(LabelAddress label)
    {
        super(X86OpType.JNE, label, null);
    }
}