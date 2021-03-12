package me.mtk.torrey;

import java.io.IOException;
import me.mtk.torrey.ast.PrettyPrinterVisitor;
import me.mtk.torrey.ast.Program;
import me.mtk.torrey.error_reporter.ErrorReporter;
import me.mtk.torrey.error_reporter.SemanticError;
import me.mtk.torrey.error_reporter.SyntaxError;
import me.mtk.torrey.lexer.Lexer;
import me.mtk.torrey.lexer.TokenList;
import me.mtk.torrey.parser.Grammar;
import me.mtk.torrey.analysis.ConstantFolderVisitor;
import me.mtk.torrey.analysis.TypeChecker;
import me.mtk.torrey.ir.IRGenVisitor;
import me.mtk.torrey.ir.IRProgram;

/**
 * The front-end of the compiler: lexical analysis, 
 * syntax analysis, semantic analysis, and intermediate
 * code generation. The output of this front-end is an
 * intermediate program. This intermediate program can then
 * be translated to literally anything (x86, LLVM, etc.).
 */
public final class TorreyFrontEnd extends TorreyCompiler
{
    public TorreyFrontEnd(TorreyConfig config, String input)
    {
        super(config, input);
    }

    public IRProgram run()
    {
        debug("Program (input to compiler): \n%s", input);

        IRProgram ir = null;

        try
        {
            final TokenList tokens = lexicalAnalysis();
            final Program dirtyAST = syntaxAnalysis(tokens);
            final Program semanticAST = semanticAnalysis(dirtyAST);
            ir = irGen(semanticAST);
        }
        catch(SyntaxError e)
        {
            System.err.println(e.getMessage());
        }
        catch(SemanticError e)
        {
            System.err.println(e.getMessage());
        }

        // In the future, if we ever want to perform optimizations on
        // the intermediate program (e.g., peephole optimization), they
        // would go here.
        
        // ...

        return ir;
    }

    /*
     * Performs lexical analysis, scanning the input string
     * for tokens.
     * 
     * @return A list of tokens.
     * @throws SyntaxError If illegal characters are encountered.
     */
    private TokenList lexicalAnalysis() throws SyntaxError
    {
        final Lexer lexer = new Lexer(
                new ErrorReporter(input), input);
        final TokenList tokens = lexer.lex();

        debug("Tokens (output from Lexer): \n%s", tokens.toString());

        // If the user has indicated that compilation should terminate
        // after performing lexical analysis, then write the tokens
        // to the configured location (stdout or file system).
        if (config.stopAtLex())
            writeAndExit(tokens.toString());

        return tokens;
    }

    /*
     * Constructs an abstract syntax tree from the list of tokens.
     * 
     * @param tokens A list of tokens.
     * @return An abstract syntax tree.
     * @throws SyntaxError If there are one or more syntax errors.
     */
    private Program syntaxAnalysis(TokenList tokens) throws SyntaxError
    {
        final PrettyPrinterVisitor ppVisitor = new PrettyPrinterVisitor();

        final Grammar grammar = new Grammar(
                new ErrorReporter(input), tokens.tokens());
        final Program program = grammar.parse();

        debug("AST (output from Grammar): \n%s", 
            ppVisitor.visit(program));

        if (config.stopAtParse())
            writeAndExit(program.toString());

        return program;
    }

    /*
     * Checks the semantics of the potentially "dirty" abstract
     * syntax tree. Performs the following functions: (1) type-checks
     * expressions, ensuring the operands supplied to an operator
     * are of the correct data type; (2) decorates the AST with
     * type information; (3) creates environments for let expressions;
     * and (4) performs any high-level compiler optimizations.
     * 
     * @param ast A potentially "dirty" abstract syntax tree whose
     * semantics ought to be analyzed.
     * @return A semantic AST.
     * @throws SemanticError If the ast is indeed "dirty".
     */
    private Program semanticAnalysis(Program ast) throws SemanticError
    {
        final PrettyPrinterVisitor ppVisitor = new PrettyPrinterVisitor();

        // Type checks operands to expressions, decorates
        // the AST with type information, and creates 
        // environments for let expressions.
        final TypeChecker typeChecker = new TypeChecker(
            new ErrorReporter(input), ast);
        typeChecker.check();

        debug("AST (output from TypeChecker): \n%s", 
            ppVisitor.visit(ast));

        // High-level optimizations (on AST)
        if (!config.disableHlOpts())
        {
            final ConstantFolderVisitor cfVistor = 
                new ConstantFolderVisitor();
            cfVistor.visit(ast);
        }

        if (config.debug() && !config.disableHlOpts())
            debug("Optimized AST (output from ConstantFolderVisitor): \n%s",
                ppVisitor.visit(ast));

        return ast;
    }

    /*
     * Generates an intermediate program from an abstract syntax tree.
     * The given abstract syntax tree MUST be semantic.
     * 
     * @param semanticAST A semantic abstract syntax tree.
     * @return The equivalent intermediate program.
     */
    private IRProgram irGen(Program semanticAST)
    {
        final IRGenVisitor irVisitor = new IRGenVisitor(semanticAST);
        final IRProgram irProgram = irVisitor.gen();

        debug("IR program (output from IRGenVisitor): \n%s", 
            irProgram.toString());

        if (config.stopAtIr()) 
            writeAndExit(irProgram.toString());

        return irProgram;
    }
}