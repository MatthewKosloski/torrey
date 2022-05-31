package me.mtk.torrey.frontend.ir.gen;

import me.mtk.torrey.frontend.ir.instructions.Quadruple;

import java.util.ArrayList;
import java.util.List;

public final class IRProgram
{
  // The three-address instructions, each represented
  // by a quadruple (op, arg1, arg2, result).
  private List<Quadruple> quads;

  public IRProgram()
  {
    quads = new ArrayList<>();
  }

  public void addQuad(Quadruple quad)
  {
    quads.add(quad);
  }

  public void addQuads(List<Quadruple> quads)
  {
    for (Quadruple quad : quads)
    {
      addQuad(quad);
    }
  }

  public List<Quadruple> quads()
  {
    return quads;
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder();

    for (int i = 0; i < quads.size(); i++)
    {
      sb.append(quads.get(i).toString())
        .append(i == quads.size() - 1 ? "" : "\n");
    }

    return sb.toString();
  }
}
