package me.mtk.torrey.Analysis;

import me.mtk.torrey.AST.Program;

public class TypeChecker {
    
    private Program unchecked;

    public TypeChecker(Program unchecked)
    {
        this.unchecked = unchecked;
    }

    public void check()
    {
        final TypeCheckerVisitor visitor = new TypeCheckerVisitor();
        unchecked.accept(visitor);
    }
}
