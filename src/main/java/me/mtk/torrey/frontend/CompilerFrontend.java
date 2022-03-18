package me.mtk.torrey.frontend;

import me.mtk.torrey.Compiler;
import me.mtk.torrey.frontend.analysis.*;
import me.mtk.torrey.frontend.ast.PrettyPrinterVisitor;
import me.mtk.torrey.frontend.ast.Program;
import me.mtk.torrey.frontend.error_reporter.*;
import me.mtk.torrey.frontend.ir.gen.IRGenerator;
import me.mtk.torrey.frontend.ir.gen.IRProgram;
import me.mtk.torrey.frontend.lexer.Lexer;
import me.mtk.torrey.frontend.lexer.TokenList;
import me.mtk.torrey.frontend.parser.Grammar;

/**
 * The front-end of the compiler: lexical analysis,
 * syntax analysis, semantic analysis, and intermediate
 * code generation. The output of this front-end is an
 * intermediate program. This intermediate program can then
 * be translated to literally anything (x86, LLVM, etc.).
 */
public final class CompilerFrontend extends Compiler
{
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
      System.exit(1);
    }
    catch(SemanticError e)
    {
      System.err.println(e.getMessage());
      System.exit(1);
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
    final Program ast = grammar.parse();

    final String prettyAST = ppVisitor.visit(ast);

    debug("AST (output from Grammar): \n%s", prettyAST);

    if (config.stopAtParse())
      writeAndExit(prettyAST);

    return ast;
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

    // Builds the let expression environments and reports
    // errors regarding the use of identifier names. No
    // type checking happens here.
    final Binder binder = new Binder(new ErrorReporter(input));
    binder.visit(ast);

    if (config.debug())
      debug("AST (output from Binder): \n%s",
        ppVisitor.visit(ast));

    // Reduces complex expressions (both arithmetic and logical).
    final ConstantFolder constantFolder = new ConstantFolder();
    constantFolder.visit(ast);

    if (config.debug())
      debug("Optimized AST (output from ConstantFolderVisitor): \n%s",
      ppVisitor.visit(ast));

    // Type-checks operands to expressions and decorates
    // the AST with type information.
    final TypeChecker typeChecker = new TypeChecker
      (new ErrorReporter(input));
    typeChecker.visit(ast);

    debug("Optimized AST (output from TypeChecker): \n%s",
      ppVisitor.visit(ast));

    if (config.debug())
      debug("Optimized AST (output from TypeChecker): \n%s",
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
    final IRGenerator irVisitor = new IRGenerator(semanticAST);
    final IRProgram irProgram = irVisitor.gen();

    debug("IR program (output from IRGenerator): \n%s",
      irProgram.toString());

    if (config.stopAtIr())
      writeAndExit(irProgram.toString());

    return irProgram;
  }
}
