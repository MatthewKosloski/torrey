package me.mtk.torrey.backend.targets.x86_64.pc.linux.gen;

import java.util.List;

public final class AssemblerDirective<T>
{
    private AssemblerDirectiveType type;
    private List<T> data;

    public AssemblerDirective(AssemblerDirectiveType type, List<T> data)
    {
        this.type = type;
        this.data = data;
    }

    public String toString()
    {
        final StringBuilder sb = new StringBuilder();
        
        sb.append(type);

        for (T datum : data)
        {
            sb.append("\t")
                .append(datum)
                .append("\n");
        }

        return sb.toString();
    }
}
