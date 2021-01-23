package me.mtk.torrey.AST;

import me.mtk.torrey.Lexer.Token;

public class IntegerExpr extends Expr
{
    public IntegerExpr(Token t) { super(t); }

    @Override
    public <T> T accept(ExprVisitor<T> visitor)
    {
        return visitor.visit(this);
    }
}
