package me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing;

public final class Temporary extends X86Address
{
    public Temporary(String tempName)
    {
        super(AddressingMode.TEMP, tempName);
    }    
}
