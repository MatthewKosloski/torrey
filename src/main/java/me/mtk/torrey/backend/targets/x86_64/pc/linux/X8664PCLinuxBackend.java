package me.mtk.torrey.backend.targets.x86_64.pc.linux;

import me.mtk.torrey.TorreyConfig;
import me.mtk.torrey.frontend.ir.gen.IRProgram;
import me.mtk.torrey.backend.targets.TargetProgram;
import me.mtk.torrey.backend.targets.TargetTriple;
import me.mtk.torrey.backend.TorreyBackend;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.assembler.Assembler;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.gen.Generator;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.gen.X86Program;

/**
 * The compiler backend that targets x86_64-pc-linux.
 */
public final class X8664PCLinuxBackend extends TorreyBackend
{

    public X8664PCLinuxBackend(TargetTriple triple)
    {
        super(triple);
    }

    public X86Program generate(IRProgram ir)
    {
        final Generator x86Gen = new Generator(ir);
        final X86Program x86Program = x86Gen.gen();

        debug("x86-64 program (output from Generator): %s\n", 
            x86Program.toString());

        if (config.stopAtCompile())
            writeAndExit(x86Program.toString());

        return x86Program;
    }

    public void assemble(TargetProgram program)
    {
        final Assembler assembler = new Assembler((X86Program) program);
        assembler.setConfig(new TorreyConfig(config));
        assembler.setInput(input);
        assembler.run(); 
    }
}