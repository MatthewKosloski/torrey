package me.mtk.torrey.X86;

public final class Register extends X86Address
{
    public Register(String register)
    {
        super(AddressingMode.REGISTER, register);
    }    
}
