package me.mtk.torrey.backend.targets.x86_64.pc.linux.gen.passes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.addressing.*;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.gen.X86Program;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.instructions.X86Inst;

public final class GenerateX86Program implements Pass<X86Program>
{
    // An x86 program that is equivalent to the input IR program.
    private X86Program x86;

    // Holds all the temporary addresses in the x86 program.
    private List<Temporary> temps;

    public GenerateX86Program(X86Program pseudoX86Program)
    {
        this.x86 = pseudoX86Program;
        this.temps = new ArrayList<>();
    }

    public X86Program pass()
    {
        // At this point, we have a "pseudo-x86" program with a, potentially
        // infinite, amount of IR temporaries. We need to replace all temporaries 
        // with base-relative stack addresses (relative to the current frame's 
        // base pointer). This is not a really effective way of using the 
        // CPU to do computations, so later we will use some algorithm 
        // (e.g., graph coloring) to perform register allocation. This 
        // will make the program much faster as it does not have to make 
        // as many trips to main memory (registers only require one CPU
        // clock cycle, main memory access requires hundreds).

        // Get a list of all the temporary registers in the order
        // in which they appear in the pseudo-x86 program.
        for (X86Inst inst : x86.instrs())
        {
            if (inst.arg1() instanceof Temporary
                && !hasTemp((Temporary) inst.arg1()))
                temps.add((Temporary) inst.arg1());
            if (inst.arg2() instanceof Temporary 
                && !hasTemp((Temporary) inst.arg2()))
                temps.add((Temporary) inst.arg2());
        }

        // Maps a temporary (e.g., "t1") to a base-relative stack 
        // address (e.g., -8(%rbp)).
        final Map<String, String> stackAddrs = new HashMap<>();
        
        // Assign a base-relative stack address to each IR temporary.
        int offset = 0;
        for (Temporary temp : temps)
            stackAddrs.put(temp.toString(), String.format("%d(%%rbp)", offset -= 8));

        // Traverse the x86 program, replacing each IR temporary with
        // its assigned base-relative stack address.
        for (X86Inst inst : x86.instrs())
        {
            if (inst.arg1() != null && inst.arg1().mode() == AddressingMode.TEMP)
                inst.setArg1(new BaseRelative(
                stackAddrs.get((String) inst.arg1().value())));
            if (inst.arg2() != null && inst.arg2().mode() == AddressingMode.TEMP)
                inst.setArg2(new BaseRelative(
                stackAddrs.get((String) inst.arg2().value())));
        }

        return x86;
    }

    private boolean hasTemp(Temporary temp)
    {
        for (Temporary t : temps)
        {
            if (t.toString().equals(temp.toString()))
                return true;
        }
        return false;
    } 
}
