package me.mtk.torrey.frontend.error_reporter;

/**
 * Represents a semantic errror thrown during semantic
 * analysis. An example of a semantic error is when
 * an operator is applied to incompatible operands.
 */

@SuppressWarnings("serial")
public class SemanticError extends Exception
{
  public SemanticError(String m) { super(m); }
}
