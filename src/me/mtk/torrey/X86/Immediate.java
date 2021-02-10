package me.mtk.torrey.X86;

public final class Immediate extends X86Address
{
    public Immediate(String immediate)
    {
        super(AddressingMode.IMMEDIATE, immediate);
    }    
}
