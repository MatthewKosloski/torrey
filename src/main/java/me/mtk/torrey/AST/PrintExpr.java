package me.mtk.torrey.ast;

import java.util.List;
import me.mtk.torrey.lexer.Token;

public class PrintExpr extends Expr
{
    public PrintExpr(Token printOp, List<Expr> exprList)
    {
        // "print" | "println"
        super(printOp);

        for (Expr expr : exprList)
            addChild(expr);
    }

    @Override
    public <T> T accept(ASTNodeVisitor<T> visitor)
    {
        return visitor.visit(this);
    }

    @Override
    public <T> T accept(ExprVisitor<T> visitor)
    {
        return visitor.visit(this);
    }
}
