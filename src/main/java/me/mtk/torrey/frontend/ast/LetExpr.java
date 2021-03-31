package me.mtk.torrey.frontend.ast;

import java.util.List;
import me.mtk.torrey.frontend.analysis.DataType;
import me.mtk.torrey.frontend.lexer.Token;
import me.mtk.torrey.frontend.symbols.Env;

public class LetExpr extends Expr implements EvaluatesToAnother
{
    private Env environment;
    private Expr evaluatesTo;

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

    public Expr evaluatesTo()
    {
        return evaluatesTo;
    }

    public void setEval(Expr expr)
    {
        evaluatesTo = expr;
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
