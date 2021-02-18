package me.mtk.torrey.ast;

import me.mtk.torrey.ir.TempAddress;
import me.mtk.torrey.lexer.Token;

public class IdentifierExpr extends Expr
{
    public IdentifierExpr(Token t) { super(t); }

    @Override
    public <T> T accept(ExprVisitor<T> visitor)
    {
        return visitor.visit(this);
    }

    @Override
    public <T> T accept(ExprIRVisitor<T> visitor, TempAddress result)
    {
        return visitor.visit(this, result);
    }
}
