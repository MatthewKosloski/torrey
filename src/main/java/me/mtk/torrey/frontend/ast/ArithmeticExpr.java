package me.mtk.torrey.frontend.ast;

import me.mtk.torrey.frontend.lexer.Token;

/**
 * Represents a binary arithmetic expression.
 */
public final class ArithmeticExpr extends BinaryExpr
{
  public ArithmeticExpr(Token tok, Expr first, Expr second)
  {
    super(tok, first, second, DataType.INTEGER);
  }

  @Override
  public void accept(ASTNodeVisitor visitor)
  {
    visitor.visit(this);
  }
}
