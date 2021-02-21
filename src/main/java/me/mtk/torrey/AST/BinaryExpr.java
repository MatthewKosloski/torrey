package me.mtk.torrey.ast;

import me.mtk.torrey.lexer.Token;

public class BinaryExpr extends Expr
{
    public BinaryExpr(Token binOp, Expr first, Expr second)
    {
        // "+" | "-" | "*" | "/"
        super(binOp);
        
        addChild(first);
        addChild(second);
    }

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
