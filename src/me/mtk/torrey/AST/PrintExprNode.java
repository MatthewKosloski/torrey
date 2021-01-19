package me.mtk.torrey.AST;

import java.util.List;
import me.mtk.torrey.Lexer.Token;

public class PrintExprNode extends ExprNode
{
    public PrintExprNode(Token printOp, List<ExprNode> exprList)
    {
        // "print" | "println"
        super(printOp);

        for (ExprNode expr : exprList)
            addChild(expr);
    }
}
