package me.mtk.torrey.frontend.analysis;

import me.mtk.torrey.frontend.ast.*;
import me.mtk.torrey.frontend.ast.Expr.DataType;
import me.mtk.torrey.frontend.error_reporter.*;
import me.mtk.torrey.frontend.lexer.*;
import me.mtk.torrey.frontend.symbols.*;

/**
 * Traverses the AST, checking the types of expressions and
 * the operands to operators. Also, determines the evaluation
 * types of identifiers, let expressions, and if expressions.
 */
public final class TypeChecker implements ASTNodeVisitor
{
  // A reference to the error reporter that will
  // report any semantic errors during type checking.
  private ErrorReporter reporter;

  // The current environment.
  private Env top;

  // The last evaluated type.
  private Expr.DataType nextType;

  /**
   * Constructs a new TypeChecker that walks an
   * AST starting at the Program node checking for type
   * errors.
   *
   * @param reporter The reporter reponsible for accumulating
   * and throwing SemanticErrors.
   */
  public TypeChecker(ErrorReporter reporter)
  {
    this.reporter = reporter;
    top = new Env(null);
  }

  /**
   * Type checks the entire program, visiting every
   * child node.
   *
   * @param program The program to be type checked.
   * @return The DataType of the program.
   */
  public void visit(Program program)
  {
    try
    {
      for (ASTNode child : program.children())
        child.accept(this);

      reporter.reportSemanticErrors("Encountered one or more semantic"
        + " errors during type checking:");
    }
    catch (SemanticError e)
    {
      System.err.println(e.getMessage());
      System.exit(1);
    }
  }

  /**
   * Type checks a comparison expression.
   *
   * @param expr The binary expression to be type checked.
   * @return Expr.DataType.BOOLEAN.
   */
  public void visit(CompareExpr expr)
  {
    final Expr first = (Expr) expr.first();
    final Expr second = (Expr) expr.second();

    // Evaluate and type-check the operands
    first.accept(this);
    second.accept(this);

    final Token operator = expr.token();

    if (operator.isType(TokenType.LT, TokenType.LTE, TokenType.GT, TokenType.GTE))
    {
      typeCheckLessThanGreaterThanExprOperands(first, second, operator);
    }
    else if (operator.isType(TokenType.EQUAL))
    {
      typeCheckEqualityExprOperands(first, second, operator);
    }

    nextType = expr.evalType();
  }

  /**
   * Type checks a binary arithmetic expression.
   *
   * @param expr The binary arithmetic expression to be type-checked.
   * @return DataType INTEGER.
   */
  public void visit(ArithmeticExpr expr)
  {
    final Expr first = (Expr) expr.first();
    final Expr second = (Expr) expr.second();

    // Type check the operands.
    first.accept(this);
    second.accept(this);

    final Token operator = expr.token();

    if (first.evalType() != Expr.DataType.INTEGER)
    {
      // expected type Expr.DataType.INTEGER
      reporter.error(first.token(), ErrorMessages.ExpectedOperandToBe,
        operator.rawText(), Expr.DataType.INTEGER, first.evalType());
    }

    if (second.evalType() != Expr.DataType.INTEGER)
    {
      // expected type Expr.DataType.INTEGER
      reporter.error(second.token(), ErrorMessages.ExpectedOperandToBe,
        operator.rawText(), Expr.DataType.INTEGER, second.evalType());
    }

    if (first instanceof IntegerExpr &&
      second instanceof IntegerExpr &&
      operator.type() == TokenType.SLASH &&
      Integer.parseInt(second.token().rawText()) == 0)
    {
      // Both operands are primitives and integers to
      // a division operator and the denominator is 0.
      reporter.error(second.token(), ErrorMessages.DivisionByZero,
        operator.rawText());
    }

    nextType = expr.evalType();
  }

  /**
   * Type checks a boolean expression.
   *
   * @param expr The boolean expression to be type-checked.
   * @return Expr.DataType.BOOLEAN
   */
  public void visit(BooleanExpr expr)
  {
    nextType = expr.evalType();
  }

  /**
   * Type checks an integer expression.
   *
   * @param expr The integer expression to be type-checked.
   * @return Expr.DataType.INTEGER
   */
  public void visit(IntegerExpr expr)
  {
    final String rawText = expr.token().rawText();
    final String lowerLimit = (Long.MIN_VALUE + "").substring(1);
    final String upperLimit = Long.MAX_VALUE + "";

    if (expr.parent() instanceof UnaryExpr
      && rawText.length() == lowerLimit.length()
      && rawText.compareTo(lowerLimit) > 0)
    {
      reporter.error(
        expr.token(),
        ErrorMessages.IntegerUnderflow,
        rawText);
    }
    else if (!(expr.parent() instanceof UnaryExpr)
      && rawText.length() == upperLimit.length()
      && rawText.compareTo(upperLimit) > 0)
    {
      reporter.error(
        expr.token(),
        ErrorMessages.IntegerOverflow,
        rawText);
    }

    nextType = expr.evalType();
  }

  /**
   * Type checks a print expression, ensuring that each
   * operand is of type integer.
   *
   * @param expr The print expression to be type checked.
   * @return DataType PRINT.
   */
  public void visit(PrintExpr expr)
  {
    for (ASTNode child : expr.children())
    {
      final Expr childExpr = (Expr) child;

      // Type-check the child expression and its children.
      childExpr.accept(this);

      if (childExpr.evalType() == Expr.DataType.NIL)
        reporter.error(
          childExpr.token(),
          ErrorMessages.UndefinedOperandToPrint,
          childExpr.token().rawText());
    }

    nextType = Expr.DataType.NIL;
  }

  /**
   * Type checks a unary expression, ensuring that the
   * operand is of type integer.
   *
   * @param expr The unary expression to be type checked.
   * @return DataType INTEGER.
   */
  public void visit(UnaryExpr expr)
  {
    final Token operator = expr.token();
    final Expr operand = (Expr) expr.first();

    // Type check the operand.
    operand.accept(this);

    if (operand.evalType() != Expr.DataType.INTEGER)
    {
      // expected type Expr.DataType.INTEGER
      reporter.error(
        operand.token(),
        ErrorMessages.ExpectedOperandToBe,
        operator.rawText(),
        Expr.DataType.INTEGER,
        operand.evalType());
    }

    nextType = expr.evalType();
  }

  public void visit(IdentifierExpr expr)
  {
    final String id = expr.token().rawText();
    final Symbol sym = top.get(id);

    expr.setEvalType(sym.expr().evalType());
    nextType = expr.evalType();
  }

  public void visit(LetExpr expr)
  {
    if (expr.children().size() > 1)
    {
      // Cache the previous environment and create
      // a new environment.
      final Env prevEnv = top;
      top = expr.environment();

      // Type-check the child expressions.
      for (ASTNode child : expr.children())
        child.accept(this);

      // Restore the previous environment.
      top = prevEnv;

      // The type of this let expression is the same
      // as the type of its last expression.
      final Expr lastExpr = (Expr) expr.last();
      expr.setEvalType(lastExpr.evalType());
    }

    nextType = expr.evalType();
  }

  public void visit(LetBindings bindings)
  {
    // Call visit(LetBinding) to type check
    // all the bindings in this AST node.
    for (ASTNode binding : bindings.children())
      binding.accept(this);

    // A LetBindings AST does not evaluate to a
    // data type as it's not an expression.
    nextType = Expr.DataType.NIL;
  }

  public void visit(LetBinding binding)
  {
    final IdentifierExpr idExpr = (IdentifierExpr) binding.first();
    final Expr boundedExpr = (Expr) binding.second();

    // Type check the bounded expression.
    boundedExpr.accept(this);

    if (boundedExpr.evalType() == Expr.DataType.NIL)
    {
      reporter.error(idExpr.token(),
        ErrorMessages.UnexpectedBoundedExprType,
        idExpr.token().rawText(),
        boundedExpr.evalType());
    }

    // A LetBinding AST does not evaluate to a
    // data type as it's not an expression.
    nextType = Expr.DataType.NIL;
  }

  public void visit(IfExpr expr)
  {
    // Type-check the test condition and its child nodes.
    expr.test().accept(this);

    // Type-check the consequent and its child nodes.
    expr.consequent().accept(this);

    // The if-then expression evaluates to the
    // same type as its consequent (even though it
    // may never execute the consequent).
    expr.setEvalType(expr.consequent().evalType());

    nextType = expr.evalType();
  }

  public void visit(IfThenElseExpr expr)
  {
    // Type-check the test condition and its child nodes.
    expr.test().accept(this);

    // Type-check the consequent and its child nodes.
    expr.consequent().accept(this);
    final Expr.DataType consequentType = nextType;

    // Type-check the alternative and its child nodes.
    expr.alternative().accept(this);
    final Expr.DataType alternativeType = nextType;

    if (consequentType != alternativeType)
    {
      // The two branches don't have the same type
      reporter.error(
        expr.token(),
        ErrorMessages.BothBranchesToIfMustBeSameType);
    }

    expr.setEvalType(expr.consequent().evalType());

    nextType = expr.evalType();
  }

  private void typeCheckLessThanGreaterThanExprOperands(Expr first, Expr second, Token operator)
  {
    if (first.evalType() != DataType.INTEGER)
    {
      reporter.error(
        first.token(),
        ErrorMessages.ExpectedOperandToBe,
        operator.rawText(),
        Expr.DataType.INTEGER,
        first.evalType());
    }

    if (second.evalType() != DataType.INTEGER)
    {
      reporter.error(
        second.token(),
        ErrorMessages.ExpectedOperandToBe,
        operator.rawText(),
        Expr.DataType.INTEGER,
        second.evalType());
    }
  }

  private void typeCheckEqualityExprOperands(Expr first, Expr second, Token operator)
  {
    final boolean firstIsIntOrBool = first.evalType() == Expr.DataType.INTEGER
      || first.evalType() == Expr.DataType.BOOLEAN;
    final boolean secondIsIntOrBool = second.evalType() == Expr.DataType.INTEGER
      || second.evalType() == Expr.DataType.BOOLEAN;
    final boolean onlyFirstIsInt = first.evalType() == Expr.DataType.INTEGER
      && second.evalType() != Expr.DataType.INTEGER;
    final boolean onlyFirstIsBool = first.evalType() == Expr.DataType.BOOLEAN
      && second.evalType() != Expr.DataType.BOOLEAN;
    final boolean onlySecondIsInt = second.evalType() == Expr.DataType.INTEGER
      && first.evalType() != Expr.DataType.INTEGER;
    final boolean onlySecondIsBool = second.evalType() == Expr.DataType.BOOLEAN
      && first.evalType() != Expr.DataType.BOOLEAN;

    if (onlyFirstIsInt)
    {
      // The first operand is an integer but the second operand is not.
      reporter.error(
        second.token(),
        ErrorMessages.ExpectedOperandToBe,
        operator.rawText(),
        Expr.DataType.INTEGER,
        second.evalType());
    }
    else if (onlyFirstIsBool)
    {
      // The first operand is a boolean but the second operand is not.
      reporter.error(
        second.token(),
        ErrorMessages.ExpectedOperandToBe,
        operator.rawText(),
        Expr.DataType.BOOLEAN,
        second.evalType());
    }
    else if (onlySecondIsInt)
    {
      // The second operand is an integer but the first operand is not.
      reporter.error(
        first.token(),
        ErrorMessages.ExpectedOperandToBe,
        operator.rawText(),
        Expr.DataType.INTEGER,
        first.evalType());
    }
    else if (onlySecondIsBool)
    {
      // The second operand is an boolean but the first operand is not.
      reporter.error(
        first.token(),
        ErrorMessages.ExpectedOperandToBe,
        operator.rawText(),
        Expr.DataType.BOOLEAN,
        first.evalType());
    }
    else if (!firstIsIntOrBool && !secondIsIntOrBool)
    {
      // Both operands are something other than integer or boolean.
      reporter.error(
        first.token(),
        ErrorMessages.ExpectedOperandToBeEither,
        operator.rawText(),
        Expr.DataType.INTEGER,
        Expr.DataType.BOOLEAN,
        first.evalType());

      reporter.error(
        second.token(),
        ErrorMessages.ExpectedOperandToBeEither,
        operator.rawText(),
        Expr.DataType.INTEGER,
        Expr.DataType.BOOLEAN,
        second.evalType());
    }
  }

}
