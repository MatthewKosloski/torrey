package me.mtk.torrey.targets;

public final class TargetTriple
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