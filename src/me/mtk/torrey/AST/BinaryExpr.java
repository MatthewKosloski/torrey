package me.mtk.torrey.AST;

import me.mtk.torrey.IR.TempAddress;
import me.mtk.torrey.Lexer.Token;

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
