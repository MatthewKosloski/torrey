package me.mtk.torrey.frontend.ast;

/**
 * Binds an identifier to an expression in a let expression.
 */
public class LetBinding extends ASTNode
{
  public LetBinding(IdentifierExpr identifier, Expr expr)
  {
    super(identifier.token());

    addChild(identifier);
    addChild(expr);
  }

  @Override
  public void accept(ASTNodeVisitor visitor)
  {
    visitor.visit(this);
  }

  @Override
  public String toString()
  {
    return String.format("%s %s", first(), second());
  }
}
