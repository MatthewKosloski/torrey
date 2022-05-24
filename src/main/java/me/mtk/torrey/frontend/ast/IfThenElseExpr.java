package me.mtk.torrey.frontend.ast;

import me.mtk.torrey.frontend.lexer.Token;

public final class IfThenElseExpr extends IfExpr
{
  public IfThenElseExpr(Token tok, Expr test, Expr consequent, Expr alternative)
  {
    super(tok, test, consequent);
    addChild(alternative);
  }

  public Expr alternative()
  {
    return (Expr) get(2);
  }

  @Override
  public void accept(ASTNodeVisitor visitor)
  {
    visitor.visit(this);
  }
}
