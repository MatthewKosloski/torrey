package me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions;

import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.BaseRelative;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.Immediate;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.Register;

public final class Addq extends BinaryArithmetic
{
    private static final String op = "addq";

    private Addq(Register src, Register dest)
    {
        super(op, src, dest);
    }  

    private Addq(BaseRelative src, Register dest)
    {
        super(op, src, dest);
    }  

    private Addq(Register src, BaseRelative dest)
    {
        super(op, src, dest);
    }  

    private Addq(Immediate src, Register dest)
    {
        super(op, src, dest);
    }  

    private Addq(Immediate src, BaseRelative dest)
    {
        super(op, src, dest);
    }  
}
