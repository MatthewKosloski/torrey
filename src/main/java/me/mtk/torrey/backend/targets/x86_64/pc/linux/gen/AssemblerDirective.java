package me.mtk.torrey.backend.targets.x86_64.pc.linux.gen;

import java.util.List;

public final class AssemblerDirective
{
    private AssemblerDirectiveType type;
    private List<Object> data;

    public AssemblerDirective(AssemblerDirectiveType type, List<Object> data)
    {
        this.type = type;
        this.data = data;
    }

    public AssemblerDirective(AssemblerDirectiveType type)
    {
        this(type, null);
    }

    public String toString()
    {
        final StringBuilder sb = new StringBuilder();
        
        sb.append(type)
            .append("\s");

        if (data != null)
        {
            for (Object datum : data)
            {
                sb.append(datum)
                    .append("\n");
            }
        }

        return sb.toString();
    }
}
