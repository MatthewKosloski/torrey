package me.mtk.torrey.ast;

import java.util.List;

import me.mtk.torrey.analysis.DataType;
import me.mtk.torrey.lexer.Token;
import me.mtk.torrey.symbols.Env;

public class LetExpr extends Expr
{
    private Env environment;

    public LetExpr(Token letTok, LetBindings bindings, List<Expr> exprList)
    {
        // "let"
        super(letTok, DataType.UNDEFINED);

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

    public void setEnv(Env e)
    {
        environment = e;
    }

    public Env environment()
    {
        return environment;
    }
}
