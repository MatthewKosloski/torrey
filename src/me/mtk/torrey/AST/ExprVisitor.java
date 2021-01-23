package me.mtk.torrey.AST;

public interface ExprVisitor<T>
{
    public T visit(BinaryExpr expr);
    public T visit(IntegerExpr expr);
    public T visit(PrintExpr expr);
    public T visit(UnaryExpr expr);
    public T visit(Program expr);
}