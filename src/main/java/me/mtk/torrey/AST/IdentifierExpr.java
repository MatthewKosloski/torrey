package me.mtk.torrey.ast;

import me.mtk.torrey.lexer.Token;

public class IdentifierExpr extends Expr
{
    public IdentifierExpr(Token t) { super(t); }

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
}
