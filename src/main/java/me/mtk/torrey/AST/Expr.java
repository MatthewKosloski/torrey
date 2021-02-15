package me.mtk.torrey.ast;

import me.mtk.torrey.ir.TempAddress;
import me.mtk.torrey.lexer.Token;

/**
 * Represents an expression in the AST.
 */
public abstract class Expr extends ASTNode 
{
    public Expr(Token t) { super(t); }
    public abstract <T> T accept(ExprVisitor<T> visitor);
    public abstract <T> T accept(ExprIRVisitor<T> visitor, TempAddress result);
}
