package me.mtk.torrey.targets.x86_64.pc.linux;

import me.mtk.torrey.TorreyCompiler;
import me.mtk.torrey.TorreyConfig;
import me.mtk.torrey.ir.IRProgram;

public final class Backend extends TorreyCompiler
{
    private IRProgram ir;
    private X86Program x86;

    public Backend(TorreyConfig config, String input, IRProgram irProgram)
    {
        super(config, input);
        ir = irProgram;
    }

    public X86Program run()
    {
        final X86Generator x86Gen = new X86Generator(ir);
        final X86Program x86Program = x86Gen.gen();

        debug("x86-64 program (output from X86Generator): %s\n", 
            x86Program.toString());

        if (config.stopAtCompile())
            writeAndExit(x86Program.toString());

        x86 = x86Program;
        return x86Program;
    }

    public void assemble()
    {
        final X86Assembler assembler = new X86Assembler(config, input, x86);
        assembler.run(); 
    }
}
