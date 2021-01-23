package me.mtk.torrey.AST;

import me.mtk.torrey.Lexer.Token;

/**
 * Represents an expression in the AST.
 */
public abstract class Expr extends ASTNode 
{
    public Expr(Token t) { super(t); }
    public abstract <T> T accept(ExprVisitor<T> visitor);
}
