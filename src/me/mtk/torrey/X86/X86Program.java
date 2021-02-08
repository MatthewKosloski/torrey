package me.mtk.torrey.X86;

import java.util.ArrayList;
import java.util.List;

public final class X86Program 
{
    private List<X86Inst> instrs;

    public X86Program()
    {
        instrs = new ArrayList<>();
    }

    public void addInst(X86Inst inst)
    {
        instrs.add(inst);
    }

    public List<X86Inst> instrs()
    {
        return instrs;
    }

    public String toString()
    {
        final StringBuilder sb = new StringBuilder();

        for (int i = 0; i < instrs.size(); i++)
            sb.append(instrs.get(i).toString())
                .append(i == instrs.size() - 1 ? "" : "\n");
            
        return sb.toString();
    }

}
