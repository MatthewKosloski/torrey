package me.mtk.torrey.frontend.ast;

import me.mtk.torrey.frontend.lexer.Token;

public final class IfThenElseExpr extends IfExpr
{

    public IfThenElseExpr(Token tok, Expr test, Expr consequent, Expr alternative)
    {
        super(tok, test, consequent);
        addChild(alternative);
    }

    public Expr test()
    {
        return (Expr) get(0);
    }

    public Expr consequent()
    {
        return (Expr) get(1);
    }

    public Expr alternative()
    {
        return (Expr) get(2);
    }

    @Override
    public <T> T accept(ASTNodeVisitor<T> visitor)
    {
        return visitor.visit(this);
    }
}
