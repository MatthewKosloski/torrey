package me.mtk.torrey.frontend.ast;

/**
 * Expressions that implement this interface do not 
 * evaluate to themselves but rather evaluate to
 * some other expression (e.g., a child expression).
 */
public interface EvaluatesToAnother
{
    /**
     * Returns a reference to an expression
     * to which this expression evaluates.
     * 
     * @return A reference to the expression to 
     * which this expression evaluates to.
     */
    public Expr evaluatesTo();

    /**
     * Sets the expression to which this 
     * expression evaluates to.
     * 
     * @param exp The expression to which
     * this expression evaluates.
     */
    public void setEval(Expr exp);
}
