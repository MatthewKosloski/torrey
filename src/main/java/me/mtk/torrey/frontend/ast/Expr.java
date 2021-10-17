package me.mtk.torrey.frontend.ast;

import me.mtk.torrey.frontend.lexer.Token;
import me.mtk.torrey.frontend.lexer.TokenType;

/**
 * Represents an expression in the AST.
 */
public abstract class Expr extends ASTNode 
{

    // Holds the possible data types of an expression.
    public enum DataType
    {
        INTEGER,
        BOOLEAN,
        NIL
    }

    // The type that this expression evaluates to.
    private DataType evalType;

    // The truthiness of this expression within a boolean context.
    // When encountered in a boolean context, all expressions
    // are truthy except for:
    //  - nil
    //  - false
    //  - 0
    //  - (- 0)
    private boolean isTruthy;

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
        
        // By default, every expression is initially truthy until
        // its actual truthiness is determined during semantic analysis.
        isTruthy = true;
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
     * Returns the data type that this expression evaluates to.
     * 
     * @return The data type of this expression.
     */
    public DataType evalType()
    {
        return evalType;
    }

    /**
     * Indicates whether this expression evaluates to a truthy value.
     * 
     * @return True if the expression evaluates to a truthy value; 
     * false otherwise.
     */
    public boolean isTruthy()
    {
        return isTruthy;
    }

    /**
     * Indicates whether this expression evaluates to a falsy value.
     * 
     * @return True if the expression evaluates to a falsy value; 
     * false otherwise.
     */
    public boolean isFalsy()
    {
        return !isTruthy;
    }

    /**
     * Sets the truthiness of the expression to falsy.
     */
    public void makeFalsy()
    {
        isTruthy = false;
    }

    /**
     * Sets the truthiness of the expression to truthy.
     */
    public void makeTruthy()
    {
        isTruthy = true;
    }

    /**
     * Indicates whether this expression has a parent expression.
     * 
     * @return True if this expression has a parent expression; False otherwise.
     */
    public boolean hasParentExpr() 
    {
        return this.parent() instanceof Expr;
    }
}
