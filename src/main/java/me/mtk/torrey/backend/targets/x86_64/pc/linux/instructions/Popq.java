package me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions;

import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.Register;

public final class Popq extends X86Inst
{
    public Popq(Register register)
    {
        super(X86OpType.POPQ, register, null);
    }
}