package me.mtk.torrey.backend.triple;

import me.mtk.torrey.backend.TorreyBackend;

public abstract class TargetTriple
{
    private TargetArch arch;
    private TargetVendor vendor;
    private TargetSys sys;

    public TargetTriple(TargetArch arch, TargetVendor vendor, TargetSys sys)
    {
        this.arch = arch;
        this.vendor = vendor;
        this.sys = sys;
    }

    public abstract TorreyBackend makeBackend();

    public TargetArch arch()
    {
        return arch;
    }

    public TargetVendor vendor()
    {
        return vendor;
    }

    public TargetSys sys()
    {
        return sys;
    }

    public String toString()
    {
        return String.format("%s-%s-%s", arch, vendor, sys)
            .toLowerCase();
    }

}