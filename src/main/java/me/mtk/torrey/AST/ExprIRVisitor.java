package me.mtk.torrey.AST;

import me.mtk.torrey.IR.TempAddress;

public interface ExprIRVisitor<T>
{
    public T visit(BinaryExpr expr, TempAddress result);
    public T visit(IntegerExpr expr, TempAddress result);
    public T visit(UnaryExpr expr, TempAddress result);
    public T visit(PrintExpr expr);
}