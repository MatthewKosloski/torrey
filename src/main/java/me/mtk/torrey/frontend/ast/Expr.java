package me.mtk.torrey.frontend.ast;

import me.mtk.torrey.frontend.lexer.Token;
import me.mtk.torrey.frontend.lexer.TokenType;

/**
 * Represents an expression in the AST.
 */
public abstract class Expr extends ASTNode
{

  // Holds the possible data types of an expression.
  public enum DataType
  {
    INTEGER,
    BOOLEAN,
    NIL
  }

  // The type that this expression evaluates to.
  private DataType evalType;

  /**
   * Constructs a new expression AST node from the given token,
   * setting its evaluation type to the given data type.
   *
   * @param tok A token from which this AST node is derived.
   * @param type the data type for which the AST evaluates.
   */
  public Expr(Token tok, DataType type)
  {
    super(tok);
    evalType = type;
  }

  /**
   * Sets the type that this expression evaluates to.
   *
   * @param type A data type.
   */
  public void setEvalType(DataType type)
  {
    evalType = type;
  }

  /**
   * Returns the data type that this expression evaluates to.
   *
   * @return The data type of this expression.
   */
  public DataType evalType()
  {
    return evalType;
  }

  public static boolean isChildOfUnaryMinusExpr(Expr expr)
  {
    return expr.parent() instanceof UnaryExpr
      && expr.parent().token().type() == TokenType.MINUS;
  }
}
