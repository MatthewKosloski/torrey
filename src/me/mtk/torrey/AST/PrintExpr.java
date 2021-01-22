package me.mtk.torrey.AST;

import java.util.List;
import me.mtk.torrey.Lexer.Token;

public class PrintExpr extends Expr
{
    public PrintExpr(Token printOp, List<Expr> exprList)
    {
        // "print" | "println"
        super(printOp);

        for (Expr expr : exprList)
            addChild(expr);
    }
}
