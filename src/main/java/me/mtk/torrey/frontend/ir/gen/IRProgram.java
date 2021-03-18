package me.mtk.torrey.frontend.ir.gen;

import me.mtk.torrey.frontend.ir.instructions.Quadruple;
import me.mtk.torrey.frontend.ir.addressing.AddressingMode;

import java.util.ArrayList;
import java.util.List;

public final class IRProgram 
{
    // The three-address instructions, each represented
    // by a quadruple (op, arg1, arg2, result).
    private List<Quadruple> quads;

    // The list of temporary names used in the IR program. They
    // are enumerated in the order in which they are created.
    private List<String> temps;
    
    public IRProgram()
    {
        quads = new ArrayList<>();
        temps = new ArrayList<>();
    }

    public void addQuad(Quadruple quad)
    {
        // If we have a first argument and it's a temporary,
        // record the name of that temporary.
        if (quad.arg1() != null && quad.arg1().mode() == AddressingMode.TEMP)
            addTemp((String) quad.arg1().value());
        
        // If we have a second argument and it's a temporary,
        // record the name of that temporary.
        if (quad.arg2() != null && quad.arg2().mode() == AddressingMode.TEMP)
            addTemp((String) quad.arg2().value());

        // If we have a result temp address, record the name.
        if (quad.result() != null)
            addTemp((String) quad.result().value());

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

    public List<String> temps()
    {
        return temps;
    }

    public String toString()
    {
        final StringBuilder sb = new StringBuilder();

        for (int i = 0; i < quads.size(); i++)
            sb.append(quads.get(i).toString())
                .append(i == quads.size() - 1 ? "" : "\n");
            
        return sb.toString();
    }

    private void addTemp(String tempName)
    {
        if (temps.indexOf(tempName) == -1)
            temps.add(tempName);
    }
}
