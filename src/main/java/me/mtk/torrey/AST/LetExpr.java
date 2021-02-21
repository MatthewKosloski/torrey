package me.mtk.torrey.ast;

import java.util.List;
import me.mtk.torrey.ir.TempAddress;
import me.mtk.torrey.lexer.Token;
import me.mtk.torrey.symbols.Env;

public class LetExpr extends Expr
{
    private Env environment;

    public LetExpr(Token letTok, LetBindings bindings, List<Expr> exprList)
    {
        // "let"
        super(letTok);

        // The environment doesn't get created until semantic analysis.
        environment = null;

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
    public <T> T accept(ASTNodeIRVisitor<T> visitor, TempAddress result)
    {
        return visitor.visit(this, result);
    }

    public void setEnv(Env e)
    {
        environment = e;
    }

    public Env environment()
    {
        return environment;
    }
}
