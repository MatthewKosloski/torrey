package me.mtk.torrey.frontend.ast;

import me.mtk.torrey.frontend.lexer.Token;
import me.mtk.torrey.frontend.lexer.TokenType;
import me.mtk.torrey.frontend.analysis.DataType;

/**
 * Represents an expression in the AST.
 */
public abstract class Expr extends ASTNode 
{
    // The type that this expression evaluates to.
    private DataType evalType;

    // The folded expression.
    private Expr folded;

    /**
     * Constructs a new expression AST node from the given token.
     * 
     * @param t A token from which this AST node is derived.
     */
    public Expr(Token t)
    { 
        this(t, null);
    }

    /**
     * Constructs a new expression AST node from the given token,
     * setting its evaluation type to the given data type.
     * 
     * @param tok A token from which this AST node is derived.
     * @param type the data type for which the AST evaluates.
     */
    public Expr(Token tok, DataType type)
    {
        super(tok);
        evalType = type;
        folded = null;
    }

    /**
     * Returns the appropriate ConstantConvertable object depending
     * on the type of constant. If the constant is non-negative, then
     * an integer expression is returned. Else, an integer expression
     * wrapped in a unary expression is returned.
     * 
     * @param constant An integer.
     * @return An IntegerExpr if constant is non-negative; Else an UnaryExpr.
     */
    public static ConstantConvertable makeConstantExpr(int constant)
    {
        if (constant < 0)
            return new UnaryExpr(new Token(TokenType.MINUS, "-"), 
                new IntegerExpr(constant * -1));
        else 
            return new IntegerExpr(constant);
    }

    /**
     * Sets the type that this expression evaluates to.
     * 
     * @param type A data type.
     */
    public void setEvalType(DataType type)
    {
        evalType = type;
    }

    /**
     * Sets the constant folded expression.
     * 
     * @param expr The constant folded version of this expression.
     */
    public void setFoldedExpr(Expr expr)
    {
        // Check to make sure that the given
        // expression is not a refererence to
        // this expression. 
        if (expr != this)
            folded = expr;
    }

    /**
     * Returns the data type that this expression evaluates to.
     * 
     * @return The data type of this expression.
     */
    public DataType evalType()
    {
        return evalType;
    }

    /**
     * Returns the constant folded version of this expression if
     * this expression can be constant folded. If this expression
     * cannot be constant folded, then null will be retuned.
     * 
     * @return The constant folded version of this expression.
     */
    public Expr folded()
    {
        return folded;
    }

    /**
     * Indicates whether this expression has been folded by
     * the constant folder.
     * 
     * @return True if this expression has been folded; 
     * False otherwise.
     */
    public boolean isFolded()
    {
        return folded != null;
    }

    /**
     * Indicates whether this expression has been folded
     * to an integer expression.
     * 
     * @return True if this expression has been folded
     * to an integer expression; False otherwise.
     */
    public boolean hasIntegerFold()
    {
        return isFolded() && folded instanceof IntegerExpr;
    }

}
