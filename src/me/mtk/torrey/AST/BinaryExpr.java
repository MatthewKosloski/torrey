package me.mtk.torrey.AST;

import me.mtk.torrey.Lexer.Token;

public class BinaryExpr extends Expr
{
    public BinaryExpr(Token binOp, Expr first, Expr second)
    {
        // "+" | "-" | "*" | "/"
        super(binOp);
        
        addChild(first);
        addChild(second);
    }
}
