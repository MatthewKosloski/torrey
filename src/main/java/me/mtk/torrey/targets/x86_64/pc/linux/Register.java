package me.mtk.torrey.targets.x86_64.pc.linux;

public final class Register extends X86Address
{
    public Register(String register)
    {
        super(AddressingMode.REGISTER, register);
    }    
}
