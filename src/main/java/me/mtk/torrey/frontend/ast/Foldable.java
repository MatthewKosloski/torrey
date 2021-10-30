package me.mtk.torrey.frontend.ast;

/**
 * If an expression implements this interface,
 * then the expression can be reduced, or "folded",
 * to a simpler or less complex expression.
 */
public interface Foldable 
{
    /**
     * Sets the expression to which this expression
     * reduces. If this expression is a comparison,
     * then it reduces, or "folds", to a boolean
     * primitive. If this expression is an arithmetic
     * expression, then it folds to a constant integer.
     * 
     * @param fold The expression to which this expression folds.
     */
    public void setFold(Expr fold);

    /**
     * Returns a reference to the expression to which
     * this expression folds.
     * 
     * @return The folded expression.
     */
    public Expr getFold();

    /**
     * Indicates whether the foldable expression has a fold. 
     * If it doesn't, then that means the compiler wasn't able
     * to fold the expression at compile-time.
     * 
     * @return True if the foldable expression is folded; False otherwise.
     */
    public boolean hasFold();
}
