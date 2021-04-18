package me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions;

import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.BaseRelative;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.Immediate;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.Register;

public abstract class BinaryArithmetic extends X86Inst 
{
    public BinaryArithmetic(String op, Register src, Register dest)
    {
        super(op, src, dest);
    }

    public BinaryArithmetic(String op, BaseRelative src, Register dest)
    {
        super(op, src, dest);
    }

    public BinaryArithmetic(String op, Register src, BaseRelative dest)
    {
        super(op, src, dest);
    }

    public BinaryArithmetic(String op, Immediate src, Register dest)
    {
        super(op, src, dest);
    }

    public BinaryArithmetic(String op, Immediate src, BaseRelative dest)
    {
        super(op, src, dest);
    }
}
