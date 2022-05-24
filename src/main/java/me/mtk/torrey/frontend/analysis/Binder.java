package me.mtk.torrey.frontend.analysis;

import me.mtk.torrey.frontend.ast.*;
import me.mtk.torrey.frontend.error_reporter.*;
import me.mtk.torrey.frontend.symbols.*;

/**
 * Traverses the AST, building lexical scope chains for
 * the let expressions. Also, if an undefined identifier
 * is referenced or an existing identifier name is used
 * in a new declaration, then these errors are reported.
 */
public class Binder implements ASTNodeVisitor
{
  // A reference to the error reporter that will
  // report any semantic errors during type checking.
  private ErrorReporter reporter;

  // The current environment.
  private Env top;

  public Binder(ErrorReporter reporter)
  {
    this.reporter = reporter;
    top = new Env(null);
  }

  public void visit(Program program)
  {
    try
    {
      for (ASTNode child : program.children())
        child.accept(this);

      reporter.reportSemanticErrors("Encountered one"
        + " or more semantic errors during"
        + " environment building:");
    }
    catch (SemanticError e)
    {
      System.err.println(e.getMessage());
      System.exit(1);
    }
  }

  public void visit(LetExpr expr)
  {
    if (expr.children().size() != 0)
    {
      // Cache the previous environment and create
      // a new environment.
      final Env prevEnv = top;
      top = new Env(top);

      // Visit child nodes.
      for (ASTNode child : expr.children())
        child.accept(this);

      // Save the environment in the AST.
      expr.setEnv(top);

      // Restore the previous environment.
      top = prevEnv;
    }
  }

  public void visit(LetBindings bindings)
  {
    for (ASTNode child : bindings.children())
      child.accept(this);
  }

  public void visit(LetBinding binding)
  {
    final IdentifierExpr idExpr = (IdentifierExpr) binding.first();
    final Expr boundedExpr = (Expr) binding.second();

    // Visit the bounded expression.
    boundedExpr.accept(this);

    final String id = idExpr.token().rawText();
    final Symbol sym = new Symbol(id, SymCategory.VARIABLE, boundedExpr);

    if (!top.has(id))
      top.put(id, sym);
    else
      // The identifier id has already been
      // declared in this scope.
      reporter.error(idExpr.token(),
        ErrorMessages.AlreadyDeclared, id);
  }

  public void visit(IdentifierExpr expr)
  {
    final String id = expr.token().rawText();
    final Symbol sym = top.get(id);

    if (sym == null)
      reporter.error(expr.token(),
        ErrorMessages.UndefinedId, id);
  }

  public void visit(IfExpr expr)
  {
    expr.test().accept(this);
    expr.consequent().accept(this);
  }

  public void visit(IfThenElseExpr expr)
  {
    expr.test().accept(this);
    expr.consequent().accept(this);
    expr.alternative().accept(this);
  }

  public void visit(PrintExpr expr)
  {
    for (ASTNode child : expr.children())
      child.accept(this);
  }

  public void visit(ArithmeticExpr expr)
  {
    expr.first().accept(this);
    expr.second().accept(this);
  }

  public void visit(CompareExpr expr)
  {
    expr.first().accept(this);
    expr.second().accept(this);
  }

  public void visit(UnaryExpr expr)
  {
    expr.first().accept(this);
  }

  public void visit(IntegerExpr expr)
  {
  }

  public void visit(BooleanExpr expr)
  {
  }
}
