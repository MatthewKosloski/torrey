package me.mtk.torrey.AST;

import me.mtk.torrey.ErrorReporter.SemanticError;

public interface ProgramVisitor<T>
{
    public T visit(Program expr) throws SemanticError;
}