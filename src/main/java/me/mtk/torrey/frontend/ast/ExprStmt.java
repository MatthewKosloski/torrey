package me.mtk.torrey.frontend.ast;

public final class ExprStmt extends Stmt
{
    public ExprStmt(Expr expr)
    {
        super(null);
        addChild(expr);
    }

    public Expr expr()
    {
        return (Expr) children().get(0);
    }

    @Override
    public <T> T accept(ASTNodeVisitor<T> visitor)
    {
        return visitor.visit(this);
    }
}
