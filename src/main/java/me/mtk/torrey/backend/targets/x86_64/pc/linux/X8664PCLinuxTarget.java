package me.mtk.torrey.backend.targets.x86_64.pc.linux;

import me.mtk.torrey.backend.TorreyBackend;
import me.mtk.torrey.backend.triple.TargetArch;
import me.mtk.torrey.backend.triple.TargetSys;
import me.mtk.torrey.backend.triple.TargetTriple;
import me.mtk.torrey.backend.triple.TargetVendor;

public final class X8664PCLinuxTarget extends TargetTriple
{
    public X8664PCLinuxTarget()
    {
        super(TargetArch.X86_64, TargetVendor.PC, TargetSys.LINUX);
    }

    public TorreyBackend makeBackend()
    {
        return new X8664PCLinuxBackend(this);
    }
}
