package me.mtk.torrey.AST;

import me.mtk.torrey.Lexer.Token;

public class UnaryExprNode extends ExprNode
{
    public UnaryExprNode(Token unaryOp, ExprNode operand) 
    { 
        // "-"
        super(unaryOp); 

        addChild(operand);
    }
}
