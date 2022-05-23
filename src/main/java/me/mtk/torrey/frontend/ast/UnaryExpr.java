package me.mtk.torrey.frontend.ast;

import me.mtk.torrey.frontend.lexer.Token;

public final class UnaryExpr extends Expr
{
  public UnaryExpr(Token unaryOp, Expr operand)
  {
    // "-"
    super(unaryOp, DataType.INTEGER);

    addChild(operand);
  }

  @Override
  public <T> T accept(ASTNodeVisitor<T> visitor)
  {
    return visitor.visit(this);
  }
}
