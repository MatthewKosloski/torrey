package me.mtk.torrey.frontend.ast;

import java.util.List;
import me.mtk.torrey.frontend.lexer.Token;

public final class PrintStmt extends Stmt
{
    public PrintStmt(Token printOp, List<Expr> exprList)
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
}
