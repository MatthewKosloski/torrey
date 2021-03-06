package me.mtk.torrey.backend.targets.x86_64.pc.linux.gen;

import java.util.ArrayList;
import java.util.List;
import me.mtk.torrey.backend.targets.TargetProgram;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions.Label;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions.X86Inst;

public final class X86Program implements TargetProgram
{
    // Holds the list of assembler directives that
    // are part of the program's text (code) segment.
    private List<AssemblerDirective> textSegment;

    // Holds the list of x86 instructions of the program.
    private List<X86Inst> instrs;

    public X86Program()
    {
        instrs = new ArrayList<>();
        textSegment = new ArrayList<>();
    }

    public X86Program addInst(X86Inst inst)
    {
        instrs.add(inst);
        return this;
    }

    public void addTextSegmentDirective(AssemblerDirective directive)
    {
        // Before adding any directives to the text segment,
        // we must first add a .text directive to define
        // the current section as .text.
        if (textSegment.isEmpty())
            textSegment.add(new AssemblerDirective(
                AssemblerDirectiveType.TEXT));

        textSegment.add(directive);
    }

    public List<X86Inst> instrs()
    {
        return instrs;
    }

    public List<AssemblerDirective> textSegment()
    {
        return textSegment;
    }

    public String toString()
    {
        final StringBuilder sb = new StringBuilder();

        // Output the text segment.
        for (int i = 0; i < textSegment.size(); i++)
        {   
            // Indent every directive other than the 
            // first. The first directive sets the 
            // start of the .text segment.
            if (i != 0)
                sb.append("  ");       

            sb.append(textSegment.get(i));
            if (i != textSegment.size() - 1)
                sb.append("\n");  
        }

        for (int i = 0; i < instrs.size(); i++)
        {
            final X86Inst inst = instrs.get(i);
            
            // Indent every instruction except
            // for labels.
            if (!(inst instanceof Label))
                sb.append("  ");

            sb.append(inst);

            if (i != instrs.size() - 1)
                sb.append("\n");
        }

        return sb.toString();
    }
}
