package me.mtk.torrey.ast;

import me.mtk.torrey.ir.TempAddress;

public interface ASTNodeIRVisitor<T>
{
	public T visit(Program program);
    public T visit(PrintExpr expr);
    public T visit(LetExpr expr, TempAddress result);
	public T visit(LetBindings letBindings, TempAddress result);
    public T visit(LetBinding letBinding, TempAddress result);
    public T visit(BinaryExpr expr, TempAddress result);
    public T visit(UnaryExpr expr, TempAddress result);
    public T visit(IdentifierExpr expr, TempAddress result);
    public T visit(IntegerExpr expr, TempAddress result);
}