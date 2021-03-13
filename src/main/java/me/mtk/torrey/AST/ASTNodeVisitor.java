package me.mtk.torrey.ast;

public interface ASTNodeVisitor<T>
{
    public T visit(Program program);
    public T visit(LetBindings bindings);
    public T visit(LetBinding binding);
    public T visit(LetExpr expr);
    public T visit(PrintExpr expr);
    public T visit(CompareExpr expr);
    public T visit(BinaryExpr expr);
    public T visit(UnaryExpr expr);
    public T visit(IdentifierExpr expr);
    public T visit(IntegerExpr expr);
}