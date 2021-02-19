package me.mtk.torrey.ast;

import me.mtk.torrey.ir.TempAddress;
import me.mtk.torrey.lexer.Token;

public class UnaryExpr extends Expr
{
    public UnaryExpr(Token unaryOp, Expr operand) 
    { 
        // "-"
        super(unaryOp); 

        addChild(operand);
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

    @Override
    public <T> T accept(ExprIRVisitor<T> visitor, TempAddress result)
    {
        return visitor.visit(this, result);
    }
}
