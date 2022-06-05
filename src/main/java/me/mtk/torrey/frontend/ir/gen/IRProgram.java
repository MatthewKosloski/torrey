package me.mtk.torrey.frontend.ir.gen;

import java.util.List;
import me.mtk.torrey.frontend.ir.instructions.Quadruple;

/**
 * A sequence of three-address instructions, where
 * each instruction is represented by a quadruple
 * of the form (op, arg1, arg2, result).
 */
public interface IRProgram
{
  void addQuad(Quadruple quad);
  void addQuads(List<Quadruple> quads);
  List<Quadruple> quads();
}
