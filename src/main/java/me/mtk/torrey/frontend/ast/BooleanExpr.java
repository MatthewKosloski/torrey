package me.mtk.torrey.frontend.ast;

import me.mtk.torrey.frontend.analysis.DataType;
import me.mtk.torrey.frontend.lexer.Token;

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