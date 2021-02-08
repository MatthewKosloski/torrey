package me.mtk.torrey.IR;

import java.util.ArrayList;
import java.util.List;

public final class IRProgram 
{
    // The three-address instructions, each represented
    // by a quadruple (op, arg1, arg2, result).
    private List<Quadruple> quads;
    
    public IRProgram()
    {
        this.quads = new ArrayList<>();
    }

    public void addQuad(Quadruple quad)
    {
        quads.add(quad);
    }

    public void addQuads(List<Quadruple> quads)
    {
        for (Quadruple quad : quads)
            addQuad(quad);
    }

    public List<Quadruple> quads()
    {
        return quads;
    }

    public String toString()
    {
        final StringBuilder sb = new StringBuilder();
        for (Quadruple quad : quads)
            sb.append(quad).append("\n");
        return sb.toString();
    }
}
