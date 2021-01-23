package me.mtk.torrey.AST;

public interface ProgramVisitor<T>
{
    public T visit(Program expr);
}