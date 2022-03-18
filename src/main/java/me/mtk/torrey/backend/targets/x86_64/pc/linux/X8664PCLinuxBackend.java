package me.mtk.torrey.backend.targets.x86_64.pc.linux;

import me.mtk.torrey.TorreyConfig;
import me.mtk.torrey.frontend.ir.gen.IRProgram;
import me.mtk.torrey.backend.TargetProgram;
import me.mtk.torrey.backend.CompilerBackend;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.assembler.Assembler;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.gen.X86Generator;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.gen.X86Program;
import me.mtk.torrey.backend.triple.TargetTriple;

/**
 * The compiler backend that targets x86_64-pc-linux.
 */
public final class X8664PCLinuxBackend extends CompilerBackend
{

  public X8664PCLinuxBackend(TargetTriple triple)
  {
    super(triple);
  }

  public X86Program generate(IRProgram ir)
  {
    final X86Program x86Program = X86Generator.buildX86ProgramFromIRProgram(ir);

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
