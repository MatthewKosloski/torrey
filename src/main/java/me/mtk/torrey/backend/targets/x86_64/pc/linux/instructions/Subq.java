package me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions;

import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.BaseRelative;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.Immediate;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.Register;

public final class Subq extends BinaryArithmetic
{
    private static final String op = "subq";

    private Subq(Register src, Register dest)
    {
        super(op, src, dest);
    }  

    private Subq(BaseRelative src, Register dest)
    {
        super(op, src, dest);
    }  

    private Subq(Register src, BaseRelative dest)
    {
        super(op, src, dest);
    }  

    private Subq(Immediate src, Register dest)
    {
        super(op, src, dest);
    }  

    private Subq(Immediate src, BaseRelative dest)
    {
        super(op, src, dest);
    }  
}
