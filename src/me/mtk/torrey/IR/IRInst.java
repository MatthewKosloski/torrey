package me.mtk.torrey.IR;

/**
 * The base class of all intermediate language instructions.
 */
public class IRInst 
{
    // symbolizes a temporary/virtual CPU register.
    protected Address addr;

    public IRInst(Address addr)
    {
        this.addr = addr;
    }
}
