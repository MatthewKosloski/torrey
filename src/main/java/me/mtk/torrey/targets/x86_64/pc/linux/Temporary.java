package me.mtk.torrey.targets.x86_64.pc.linux;

public final class Temporary extends X86Address
{
    public Temporary(String tempName)
    {
        super(AddressingMode.TEMP, tempName);
    }    
}
