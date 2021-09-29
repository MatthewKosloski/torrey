package me.mtk.torrey.frontend.ast;

import me.mtk.torrey.frontend.lexer.Token;
import me.mtk.torrey.frontend.lexer.TokenType;

/**
 * Represents a boolean literal expression.
 */
public final class BooleanExpr extends PrimitiveExpr
{
    public BooleanExpr(Token t)
    {
        super(t, DataType.BOOLEAN);
    }

    public BooleanExpr(boolean bool)
    {
        this(new Token(bool 
            ? TokenType.TRUE 
            : TokenType.FALSE, bool + ""));
    }

    public boolean toBoolean()
    {
        return this.token().type() == TokenType.TRUE;
    }

    @Override
    public <T> T accept(ASTNodeVisitor<T> visitor)
    {
        return visitor.visit(this);
    }
}