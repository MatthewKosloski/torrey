package me.mtk.torrey.ast;

import me.mtk.torrey.analysis.DataType;
import me.mtk.torrey.lexer.Token;

public abstract class PrimitiveExpr extends Expr
{
    public PrimitiveExpr(Token tok, DataType evalType)
    {
        super(tok, evalType);
    }

    @Override
    public <T> T accept(ASTNodeVisitor<T> visitor)
    {
        return visitor.visit(this);
    }
}
