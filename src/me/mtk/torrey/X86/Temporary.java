package me.mtk.torrey.X86;

public final class Temporary extends X86Address
{
    public Temporary(String tempName)
    {
        super(AddressingMode.TEMP, tempName);
    }    
}
