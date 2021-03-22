package me.mtk.torrey.frontend.ast;

import me.mtk.torrey.frontend.analysis.DataType;
import me.mtk.torrey.frontend.lexer.Token;

public final class IfExpr extends Expr 
{
    public IfExpr(Token tok, Expr test, Expr consequent, Expr alternative)
    {
        super(tok, DataType.UNDEFINED);
        addChild(test);
        addChild(consequent);

        // If this is an if-then expression,
        // then alternative will be null.
        if (alternative != null)
            addChild(alternative);
    }

    public IfExpr(Token tok, Expr test, Expr consequent)
    {
        this(tok, test, consequent, null);
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
