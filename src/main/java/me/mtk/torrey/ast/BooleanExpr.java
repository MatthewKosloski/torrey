package me.mtk.torrey.ast;

import me.mtk.torrey.analysis.DataType;
import me.mtk.torrey.lexer.Token;

/**
 * Represents a boolean literal expression.
 */
public final class BooleanExpr extends PrimitiveExpr
{
    public BooleanExpr(Token t)
    {
        super(t, DataType.BOOLEAN);
    }
}