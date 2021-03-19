package me.mtk.torrey.frontend.ast;

public interface ASTNodeVisitor<T>
{
    // Root AST node.
    public T visit(Program program);

    // Expressions.
    public T visit(LetExpr expr);
    public T visit(LetBindings bindings);
    public T visit(LetBinding binding);
    public T visit(BinaryExpr expr);
    public T visit(UnaryExpr expr);
    public T visit(IdentifierExpr expr);
    public T visit(PrimitiveExpr expr);

    // Statements.
    public T visit(PrintStmt stmt);
    public T visit(ExprStmt stmt);
}