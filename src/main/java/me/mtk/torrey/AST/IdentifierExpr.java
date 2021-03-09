package me.mtk.torrey.ast;

import me.mtk.torrey.analysis.DataType;
import me.mtk.torrey.lexer.Token;

public class IdentifierExpr extends Expr
{
    public IdentifierExpr(Token t) 
    {
        super(t, DataType.UNDEFINED);
    }

    @Override
    public <T> T accept(ASTNodeVisitor<T> visitor)
    {
        return visitor.visit(this);
    }
}
