package me.mtk.torrey.ast;

/**
 * Binds an identifier to an expression in a let expression.
 */
public class LetBinding extends ASTNode
{
    public LetBinding(IdentifierExpr identifier, Expr expr) 
    {
        super(identifier.token());

        addChild(identifier);
        addChild(expr);
    }
}
