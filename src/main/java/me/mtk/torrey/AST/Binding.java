package me.mtk.torrey.ast;

/**
 * Binds an identifier to an expression in a let expression.
 */
public class Binding extends ASTNode
{
    public Binding(IdentifierExpr identifier, Expr expr) 
    {
        super(identifier.token());

        addChild(identifier);
        addChild(expr);
    }
}
