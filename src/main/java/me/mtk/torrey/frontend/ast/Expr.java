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
}
