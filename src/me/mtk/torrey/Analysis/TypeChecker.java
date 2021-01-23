package me.mtk.torrey.Analysis;

import me.mtk.torrey.AST.Program;

public final class TypeChecker 
{
    // The program that is to be type checked.
    private Program program;

    /**
     * Constructs a new type checker, initializing
     * it with the specified Program ASTNode to be
     * subsequently type checked.
     * 
     * @param program A Program ASTNode.
     */
    public TypeChecker(Program program)
    {
        this.program = program;
    }

    /**
     * Initiates the type checking of the program.
     */
    public void check()
    {
        // Calls the TypeCheckerVisitor's overloaded visit()
        // method that takes a Program ASTNode as an argument.
        program.accept(new TypeCheckerVisitor());
    }
}
