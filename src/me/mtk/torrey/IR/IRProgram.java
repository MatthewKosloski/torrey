package me.mtk.torrey.IR;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class IRProgram 
{
    // The three-address instructions, each represented
    // by a quadruple (op, arg1, arg2, result).
    private List<Quadruple> quads;

    // Implementation of a symbol table with monolithic scope
    private Map<String, Integer> symtable;
    
    public IRProgram()
    {
        this.quads = new ArrayList<>();
        this.symtable = new HashMap<>();
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

    public void define(String name, int constant)
    {
        symtable.put(name, constant);
    }

    public int resolve(String name)
    {
        return symtable.get(name);
    }

    public List<Quadruple> quads()
    {
        return quads;
    }

    public Map<String, Integer> symtable()
    {
        return symtable;
    }

    public String toString()
    {
        final StringBuilder sb = new StringBuilder();
        for (Quadruple quad : quads)
            sb.append(quad).append("\n");
        return sb.toString();
    }

}
