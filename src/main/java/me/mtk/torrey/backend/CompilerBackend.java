package me.mtk.torrey.backend;

import me.mtk.torrey.Compiler;
import me.mtk.torrey.backend.triple.TargetTriple;
import me.mtk.torrey.frontend.ir.gen.IRProgram;

public abstract class CompilerBackend extends Compiler
{
  private TargetTriple triple;

  public CompilerBackend(TargetTriple triple)
  {
    this.triple = triple;
  }

  /**
   * Returns the target triple of this backend.
   *
   * @return A target triple.
   */
  public TargetTriple triple()
  {
    return triple;
  }

  /**
   * Generates the target program.
   *
   * @return TargetProgram The program generated by
   * this compiler backend.
   *
   * @param irProgram The IRProgram from which target
   * code should be generated.
   */
  public abstract TargetProgram generate(IRProgram irProgram);

  /**
   * Assembles the target program into a native
   * executable specific to the target's architecture.
   *
   * @param program The program to be assembled.
   */
  public abstract void assemble(TargetProgram program);
}
