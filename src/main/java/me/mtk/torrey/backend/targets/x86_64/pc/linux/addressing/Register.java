package me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing;

public final class Register extends X86Address
{
    public Register(Registers register)
    {
        super(AddressingMode.REGISTER, register);
    }    
}
