package me.mtk.torrey.ast;

import me.mtk.torrey.analysis.DataType;
import me.mtk.torrey.lexer.Token;

public class BinaryExpr extends Expr
{
    public BinaryExpr(Token binOp, Expr first, Expr second)
    {
        // "+" | "-" | "*" | "/"
        super(binOp, DataType.INTEGER);
        
        addChild(first);
        addChild(second);
    }

    @Override
    public <T> T accept(ASTNodeVisitor<T> visitor)
    {
        return visitor.visit(this);
    }
}
