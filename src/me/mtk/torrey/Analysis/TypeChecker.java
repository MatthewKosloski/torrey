package me.mtk.torrey.Analysis;

import me.mtk.torrey.AST.Program;
import me.mtk.torrey.ErrorReporter.ErrorReporter;
import me.mtk.torrey.ErrorReporter.SemanticError;

public final class TypeChecker 
{
    // The TypeCheckVisitor that walks the
    // Program ASTNode to perform type checking.
    private TypeCheckerVisitor visitor;

    // The program that is to be type checked.
    private Program program;

    /**
     * Constructs a new type checker, initializing
     * it with the specified Program ASTNode to be
     * subsequently type checked.
     * 
     * @param program A Program ASTNode.
     */
    public TypeChecker(ErrorReporter reporter, Program program)
    {
        this.visitor = new TypeCheckerVisitor(reporter);
        this.program = program;
    }

    /**
     * Initiates the type checking of the program.
     */
    public void check() throws SemanticError
    {
        // Calls the TypeCheckerVisitor's overloaded visit()
        // method that takes a Program ASTNode as an argument.
        program.accept(visitor);
    }
}
