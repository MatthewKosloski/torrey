package me.mtk.torrey.frontend.ast;

import me.mtk.torrey.frontend.analysis.DataType;
import me.mtk.torrey.frontend.lexer.Token;

public class IdentifierExpr extends Expr implements EvaluatesToAnother
{

    private Expr evaluatesTo;

    public IdentifierExpr(Token t) 
    {
        super(t, DataType.UNDEFINED);
    }

    public Expr evaluatesTo()
    {
        return evaluatesTo;
    }

    public void setEval(Expr expr)
    {
        evaluatesTo = expr;
    }

    @Override
    public <T> T accept(ASTNodeVisitor<T> visitor)
    {
        return visitor.visit(this);
    }
}
