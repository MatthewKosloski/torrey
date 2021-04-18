package me.mtk.torrey.backend.targets.x86_64.pc.linux.gen;

public enum AssemblerDirectiveType 
{
    GLOBL ("globl"),
    TEXT  ("text");

    private final String name;

    AssemblerDirectiveType(String name)
    {
        this.name = name;
    }

    public String toString()
    {
        return String.format(".%s", name);
    }
}
