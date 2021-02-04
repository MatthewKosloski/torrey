package me.mtk.torrey.AST;

public interface ProgramIRVisitor<T>
{
    public T visit(Program expr);
}