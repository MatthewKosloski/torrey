package me.mtk.torrey.frontend.ast;

import me.mtk.torrey.frontend.analysis.DataType;
import me.mtk.torrey.frontend.lexer.Token;

public class BinaryExpr extends Expr implements Foldable
{

    // The expression that this expression reduces to.
    private Expr fold;

    public BinaryExpr(Token tok, Expr first, Expr second, DataType evalType)
    {
        super(tok, evalType);
        addChild(first);
        addChild(second);
    }

    public void setFold(Expr fold)
    {
        this.fold = fold;
    }

    public Expr getFold()
    {
        return fold;
    }

    @Override
    public <T> T accept(ASTNodeVisitor<T> visitor)
    {
        return visitor.visit(this);
    }
}
