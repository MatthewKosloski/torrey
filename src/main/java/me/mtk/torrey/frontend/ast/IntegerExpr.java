package me.mtk.torrey.frontend.ast;

import me.mtk.torrey.frontend.lexer.Token;
import me.mtk.torrey.frontend.lexer.TokenType;

/**
 * Represents an integer literal expression.
 */
public final class IntegerExpr extends PrimitiveExpr
    implements ConstantConvertable
{
  public IntegerExpr(Token t)
  {
    super(t, DataType.INTEGER);
  }

  public IntegerExpr(long constant)
  {
    this(new Token(TokenType.INTEGER, constant + ""));
  }

  public long toConstant()
  {
    return Expr.isChildOfUnaryMinusExpr(this)
      ? Long.parseLong(String.format("-%s", token().rawText()))
      : Long.parseLong(token().rawText());
  }

  @Override
  public void accept(ASTNodeVisitor visitor)
  {
    visitor.visit(this);
  }
}
