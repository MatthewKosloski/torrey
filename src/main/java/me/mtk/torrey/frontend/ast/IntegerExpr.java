package me.mtk.torrey.frontend.ast;

import me.mtk.torrey.frontend.analysis.DataType;
import me.mtk.torrey.frontend.lexer.Token;
import me.mtk.torrey.frontend.lexer.TokenType;

/**
 * Represents an integer literal expression.
 */
public final class IntegerExpr extends PrimitiveExpr 
    implements ConstantConvertable
{
    public IntegerExpr(Token t)
    {
        super(t, DataType.INTEGER);
    }

    public IntegerExpr(int constant)
    {
        this(new Token(TokenType.INTEGER, constant + ""));
    }

    public int toConstant()
    {
        return Integer.parseInt(token().rawText());
    }

    @Override
    public <T> T accept(ASTNodeVisitor<T> visitor)
    {
        return visitor.visit(this);
    }
}
