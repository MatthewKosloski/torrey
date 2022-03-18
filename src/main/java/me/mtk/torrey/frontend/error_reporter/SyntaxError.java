package me.mtk.torrey.frontend.error_reporter;

/**
 * Represents a syntax error that is thrown either during
 * lexical or syntax analysis. Examples of syntax errors
 * include illegal characters encountered during lexing
 * or unexpected characters during parsing.
 */

@SuppressWarnings("serial")
public class SyntaxError extends Exception
{
  public SyntaxError(String m) { super(m); }
}
