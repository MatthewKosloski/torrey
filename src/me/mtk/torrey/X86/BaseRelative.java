package me.mtk.torrey.X86;

public final class BaseRelative extends X86Address
{
    public BaseRelative(String tempName)
    {
        super(AddressingMode.BASEREL, tempName);
    }    
}
