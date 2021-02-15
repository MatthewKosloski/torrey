package me.mtk.torrey.ast;

public interface ExprVisitor<T>
{
    public T visit(BinaryExpr expr);
    public T visit(IntegerExpr expr);
    public T visit(PrintExpr expr);
    public T visit(UnaryExpr expr);
}