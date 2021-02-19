package me.mtk.torrey.ast;

import java.util.List;
import me.mtk.torrey.ir.TempAddress;
import me.mtk.torrey.lexer.Token;

public class LetExpr extends Expr
{
    public LetExpr(Token letTok, LetBindings bindings, List<Expr> exprList)
    {
        // "let"
        super(letTok);

        // (identifier expr)*
        addChild(bindings);

        // expr*
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

    @Override
    public <T> T accept(ExprIRVisitor<T> visitor, TempAddress result)
    {
        return visitor.visit(this, result);
    }
}
