package me.mtk.torrey.ast;

import me.mtk.torrey.ir.TempAddress;

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

    @Override
    public <T> T accept(ASTNodeVisitor<T> visitor)
    {
        return visitor.visit(this);
    }

    @Override
    public <T> T accept(ASTNodeIRVisitor<T> visitor, TempAddress result)
    {
        return visitor.visit(this, result);
    }

    @Override
    public String toString()
    {
        return String.format("%s %s", first(), second());
    }
}
