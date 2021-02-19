package me.mtk.torrey.ast;

import me.mtk.torrey.ir.TempAddress;
import me.mtk.torrey.lexer.Token;

public class IntegerExpr extends Expr
{
    public IntegerExpr(Token t) { super(t); }

    @Override
    public <T> T accept(ASTNodeVisitor<T> visitor)
    {
        return visitor.visit(this);
    }

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
