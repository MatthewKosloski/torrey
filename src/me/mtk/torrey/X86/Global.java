package me.mtk.torrey.X86;

public final class Global extends X86Address
{
    public Global(String name)
    {
        super(AddressingMode.GLOBAL, name);
    }    
}
