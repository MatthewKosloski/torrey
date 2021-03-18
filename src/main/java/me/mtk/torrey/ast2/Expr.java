package me.mtk.torrey.ast;

import me.mtk.torrey.lexer.Token;
import me.mtk.torrey.analysis.DataType;

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
