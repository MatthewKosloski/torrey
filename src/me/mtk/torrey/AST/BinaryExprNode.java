package me.mtk.torrey.AST;

import me.mtk.torrey.Lexer.Token;

public class BinaryExprNode extends ExprNode
{
    public BinaryExprNode(Token binOp, ExprNode first, ExprNode second)
    {
        // "+" | "-" | "*" | "/"
        super(binOp);
        
        addChild(first);
        addChild(second);
    }
}
