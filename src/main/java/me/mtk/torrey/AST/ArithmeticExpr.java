package me.mtk.torrey.ast;

import me.mtk.torrey.analysis.DataType;
import me.mtk.torrey.lexer.Token;

/**
 * Represents a binary arithmetic expression.
 */
public final class ArithmeticExpr extends BinaryExpr
{
    public ArithmeticExpr(Token tok, Expr first, Expr second)
    {
        super(tok, first, second, DataType.INTEGER);
    }    
}
