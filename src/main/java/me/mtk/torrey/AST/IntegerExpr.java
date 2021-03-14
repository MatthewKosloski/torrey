package me.mtk.torrey.ast;

import me.mtk.torrey.analysis.DataType;
import me.mtk.torrey.lexer.Token;
import me.mtk.torrey.lexer.TokenType;

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
}
