package me.mtk.torrey.x86;

public final class Immediate extends X86Address
{
    public Immediate(String immediate)
    {
        super(AddressingMode.IMMEDIATE, immediate);
    }    
}
