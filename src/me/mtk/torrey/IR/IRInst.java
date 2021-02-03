package me.mtk.torrey.IR;

public class IRInst 
{
    // symbolizes a temporary/virtual CPU register.
    protected Address addr;

    public IRInst(Address addr)
    {
        this.addr = addr;
    }

    public Address addr()
    {
        return addr;
    }
}
