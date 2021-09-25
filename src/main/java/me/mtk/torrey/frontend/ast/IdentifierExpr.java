package me.mtk.torrey.frontend.ast;

import me.mtk.torrey.frontend.lexer.Token;

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
