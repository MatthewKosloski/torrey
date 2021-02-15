package me.mtk.torrey.x86;

public final class Global extends X86Address
{
    public Global(String name)
    {
        super(AddressingMode.GLOBAL, name);
    }    
}
