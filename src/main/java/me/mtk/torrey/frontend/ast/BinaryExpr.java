package me.mtk.torrey.frontend.ast;

import me.mtk.torrey.frontend.lexer.Token;

public abstract class BinaryExpr extends Expr
{
  public BinaryExpr(Token tok, Expr first, Expr second, DataType evalType)
  {
    super(tok, evalType);
    addChild(first);
    addChild(second);
  }
}
