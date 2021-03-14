package me.mtk.torrey.ast;

import me.mtk.torrey.analysis.DataType;
import me.mtk.torrey.lexer.Token;

public final class CompareExpr extends BinaryExpr
{
    public CompareExpr(Token tok, Expr first, Expr second)
    {
        super(tok, first, second, DataType.BOOLEAN);
    }    
}
