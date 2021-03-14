package me.mtk.torrey.ast;

import me.mtk.torrey.analysis.DataType;
import me.mtk.torrey.lexer.Token;

public class BinaryExpr extends Expr
{
    public BinaryExpr(Token tok, Expr first, Expr second, DataType evalType)
    {
        super(tok, evalType);
        addChild(first);
        addChild(second);
    }

    @Override
    public <T> T accept(ASTNodeVisitor<T> visitor)
    {
        return visitor.visit(this);
    }
}
