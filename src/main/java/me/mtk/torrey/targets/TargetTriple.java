package me.mtk.torrey.targets;

import me.mtk.torrey.TorreyBackend;

public final class TargetTriple
{
    private TorreyBackend backend;
    private TargetArch arch;
    private TargetVendor vendor;
    private TargetSys sys;

    public TargetTriple(TorreyBackend backend, TargetArch arch, 
        TargetVendor vendor, TargetSys sys)
    {
        this.backend = backend;
        this.arch = arch;
        this.vendor = vendor;
        this.sys = sys;
    }

    public TorreyBackend backend()
    {
        return backend;
    }

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
        return String.format("%s-%s-%s\n", arch, vendor, sys);
    }

}