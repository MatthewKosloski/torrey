package me.mtk.torrey.frontend.ast;

import me.mtk.torrey.frontend.lexer.Token;

public final class CompareExpr extends BinaryExpr
{
  public CompareExpr(Token tok, Expr first, Expr second)
  {
    super(tok, first, second, DataType.BOOLEAN);
  }

  @Override
  public <T> T accept(ASTNodeVisitor<T> visitor)
  {
    return visitor.visit(this);
  }
}
