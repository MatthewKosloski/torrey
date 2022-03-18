package me.mtk.torrey.frontend.ast;

import java.util.List;

/**
 * Stores zero or more let bindings, where
 * each binding is an identifier expression
 * mapped to an expression.
 */
public class LetBindings extends ASTNode
{
  public LetBindings(List<LetBinding> bindings)
  {
    super(null);

    for (LetBinding binding : bindings)
      addChild(binding);
  }

  @Override
  public <T> T accept(ASTNodeVisitor<T> visitor)
  {
    return visitor.visit(this);
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder();

    sb.append("[");
    for (int i = 0; i < children().size(); i++)
      sb.append(children().get(i).toString())
        .append(i == children().size() - 1 ? "" : " ");
    sb.append("]");

    return sb.toString();
  }
}
