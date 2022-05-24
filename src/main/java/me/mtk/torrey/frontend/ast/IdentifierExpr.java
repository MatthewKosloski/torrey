package me.mtk.torrey.frontend.ast;

import me.mtk.torrey.frontend.lexer.Token;

public class IdentifierExpr extends Expr
{
  public IdentifierExpr(Token t)
  {
    super(t, DataType.NIL);
  }

  @Override
  public void accept(ASTNodeVisitor visitor)
  {
    visitor.visit(this);
  }
}
