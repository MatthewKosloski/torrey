package me.mtk.torrey.frontend.ast;

import me.mtk.torrey.frontend.lexer.Token;

public class IfExpr extends Expr
{
    public IfExpr(Token tok, Expr test, Expr consequent)
    {
        super(tok, DataType.UNDEFINED);
        addChild(test);
        addChild(consequent);
    }

    public Expr test()
    {
        return (Expr) get(0);
    }

    public Expr consequent()
    {
        return (Expr) get(1);
    }

    @Override
    public <T> T accept(ASTNodeVisitor<T> visitor)
    {
        return visitor.visit(this);
    }
}
