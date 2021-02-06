package me.mtk.torrey.X86;

public class X86Inst 
{
    private String op;
    private String src;
    private String dest;

    public X86Inst(String op, String src, String dest)
    {
        this.op = op;
        this.src = src;
        this.dest = dest;
    }

    public String toString()
    {
        return String.format("%s %s, %s");
    }

}
