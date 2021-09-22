package me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions;

import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.Register;

public final class Pushq extends X86Inst
{
    public Pushq(Register register)
    {
        super(X86OpType.PUSHQ, register, null);
    }
}