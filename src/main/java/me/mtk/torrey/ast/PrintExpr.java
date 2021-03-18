package me.mtk.torrey.ast;

import java.util.List;

import me.mtk.torrey.analysis.DataType;
import me.mtk.torrey.lexer.Token;

public class PrintExpr extends Expr
{
    public PrintExpr(Token printOp, List<Expr> exprList)
    {
        // "print" | "println"
        super(printOp, DataType.UNDEFINED);

        for (Expr expr : exprList)
            addChild(expr);
    }

    @Override
    public <T> T accept(ASTNodeVisitor<T> visitor)
    {
        return visitor.visit(this);
    }
}
