package me.mtk.torrey.frontend.ir.gen;

import java.util.ArrayList;
import java.util.List;
import me.mtk.torrey.frontend.ir.instructions.Quadruple;

public final class DefaultIRProgram implements IRProgram
{
  private List<Quadruple> quads;

  public DefaultIRProgram()
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
  public boolean equals(Object o)
  {
    if (o == this)
    {
      return true;
    }

    if (!(o instanceof DefaultIRProgram))
    {
      return false;
    }

    DefaultIRProgram that = (DefaultIRProgram)o;

    if (this.quads.size() != that.quads.size())
    {
      return false;
    }

    return this.quads.equals(that.quads);
  }

  @Override
  public int hashCode()
  {
    return this.quads.hashCode();
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
