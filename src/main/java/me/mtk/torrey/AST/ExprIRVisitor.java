package me.mtk.torrey.ast;

import me.mtk.torrey.ir.TempAddress;

public interface ExprIRVisitor<T>
{
    public T visit(BinaryExpr expr, TempAddress result);
    public T visit(IntegerExpr expr, TempAddress result);
    public T visit(UnaryExpr expr, TempAddress result);
    public T visit(PrintExpr expr);
}