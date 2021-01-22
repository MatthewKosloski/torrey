package me.mtk.torrey.AST;

import me.mtk.torrey.Lexer.Token;

public class UnaryExpr extends Expr
{
    public UnaryExpr(Token unaryOp, Expr operand) 
    { 
        // "-"
        super(unaryOp); 

        addChild(operand);
    }
}
