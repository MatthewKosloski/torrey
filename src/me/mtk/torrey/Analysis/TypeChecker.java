package me.mtk.torrey.Analysis;

import me.mtk.torrey.AST.Program;

public class TypeChecker 
{
    
    private Program program;

    public TypeChecker(Program program)
    {
        this.program = program;
    }

    public void check()
    {
        final TypeCheckerVisitor visitor = new TypeCheckerVisitor();
        program.accept(visitor);
    }
}
