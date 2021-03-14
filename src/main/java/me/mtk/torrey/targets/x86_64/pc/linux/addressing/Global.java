package me.mtk.torrey.targets.x86_64.pc.linux.addressing;

public final class Global extends X86Address
{
    public Global(String name)
    {
        super(AddressingMode.GLOBAL, name);
    }    
}
