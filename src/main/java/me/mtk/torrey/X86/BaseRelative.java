package me.mtk.torrey.x86;

public final class BaseRelative extends X86Address
{
    public BaseRelative(String tempName)
    {
        super(AddressingMode.BASEREL, tempName);
    }    
}
