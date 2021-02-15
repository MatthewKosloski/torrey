package me.mtk.torrey.ast;

import me.mtk.torrey.error_reporter.SemanticError;

public interface ProgramVisitor<T>
{
    public T visit(Program expr) throws SemanticError;
}