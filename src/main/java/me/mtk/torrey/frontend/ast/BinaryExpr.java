package me.mtk.torrey.frontend.ast;

import me.mtk.torrey.frontend.lexer.Token;

public abstract class BinaryExpr extends Expr implements Foldable
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
        // If the fold is equal to ourselves, then don't
        // assign it as there's no point. Also, only assign
        // the fold to us if we don't already have one (this
        // prevents the overriding of folds).
        if (!fold.equals(this) && this.fold == null)
            this.fold = fold;
    }

    public Expr getFold()
    {
        return fold;
    }
}
