package me.mtk.torrey.backend.targets.x86_64.pc.linux.gen;

import java.util.ArrayList;
import java.util.List;
import me.mtk.torrey.backend.targets.TargetProgram;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions.Label;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions.X86Inst;

public final class X86Program implements TargetProgram
{
    private List<AssemblerDirective> directives;
    private List<X86Inst> instrs;

    public X86Program()
    {
        instrs = new ArrayList<>();
        directives = new ArrayList<>();
    }

    public X86Program addInst(X86Inst inst)
    {
        instrs.add(inst);
        return this;
    }

    public void addDirective(AssemblerDirective directive)
    {
        directives.add(directive);
    }

    public List<X86Inst> instrs()
    {
        return instrs;
    }

    public List<AssemblerDirective> directives()
    {
        return directives;
    }

    public String toString()
    {
        final StringBuilder sb = new StringBuilder();

        for (int i = 0; i < directives.size(); i++)
        {          
            sb.append(directives.get(i));
            if (i != directives.size() - 1)
                sb.append("\n");  
        }

        for (int i = 0; i < instrs.size(); i++)
        {
            final X86Inst inst = instrs.get(i);
            
            // Indent every instruction except
            // for labels.
            if (!(inst instanceof Label))
                sb.append("\s\s");

            sb.append(inst);

            if (i != instrs.size() - 1)
                sb.append("\n");
        }

        return sb.toString();
    }
}
