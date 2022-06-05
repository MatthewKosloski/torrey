package me.mtk.torrey.frontend.error_reporter;

import java.util.Stack;
import me.mtk.torrey.frontend.lexer.Token;

public class ErrorReporter
{

  // The input program.
  private String input;

  // Accumulate the error messages in a stack.
  private Stack<String> errMsgStack;


  /**
   * Constructs a new ErrorReporter object, initializing
   * it with the input program from which to extract substrings
   * for helpful error messages.
   *
   * @param input An input program.
   */
  public ErrorReporter(final String input)
  {
    this.input = input;
    errMsgStack = new Stack<>();
  }


  /**
   * Reports any accumulated errors by displaying the provided
   * message to the standard errors stream and throwing a
   * SyntaxError exception.
   *
   * @param message The message to preface the error messages
   * that are being reported.
   * @throws SyntaxError
   */
  public void reportSyntaxErrors(String message) throws SyntaxError
  {
    if (errMsgStack.size() > 0)
    {
      // We have errors to report.
      System.err.println(message);
      throw new SyntaxError(this.toString());
    }
  }

  /**
   * Reports any accumulated errors by displaying the provided
   * message to the standard errors stream and throwing a
   * SemanticError exception.
   *
   * @param message The message to preface the error messages
   * that are being reported.
   * @throws SemanticError
   */
  public void reportSemanticErrors(String message) throws SemanticError
  {
    if (errMsgStack.size() > 0)
    {
      // We have errors to report.
      System.err.println(message);
      throw new SemanticError(this.toString());
    }
  }

  /**
   * Constructs an error message, appending it to the stderr buffer.
   *
   * @param tok The offending token. Includes location information where
   * the error occurred.
   * @param template An error message in the form of a format string.
   * @param args The strings that replace the format specifies
   * within the format string.
   */
  public void error(Token tok, String template, Object... args)
  {
    // Find the index (into input) of the last token on this line
    int endIndex = tok.beginLineIndex();
    while (endIndex < input.length() && input.charAt(endIndex) != '\n')
      endIndex++;

    final String offendingLine = input.substring(
      tok.beginLineIndex(), endIndex);

    final StringBuilder str = new StringBuilder();

    // Count number of leading and trailing white space characters
    int trimCount = 0;
    if (!offendingLine.isBlank())
    {
      int i = 0;
      char c = offendingLine.charAt(i);

      // Count leading white space characters
      while (c != '\n' && Character.isWhitespace(c) && i < offendingLine.length())
      {
        trimCount++;
        c = offendingLine.charAt(++i);
      }

      // Count trailing white space characters
      i = offendingLine.length() - 1;
      while (c != '\n' && Character.isWhitespace(c) && i >= 0)
      {
        trimCount++;
        c = offendingLine.charAt(--i);
      }
    }

    str.append("\n")
      .append(String.format(template, args))
      .append(" ")
      // Print the line number and column number of the offending token
      .append(tok.startPos())
      .append("\n\n")
      .append(offendingLine.trim())
      .append("\n");

    // Print "^^^...", pointing to the offending token.

    // move the pointer under the token
    for (int i = 0; i < tok.beginIndex() -
      tok.beginLineIndex() - trimCount; i++)
      str.append(" ");

    // print the pointer across the token
    for (int i = 0; i < tok.rawText().length(); i++)
      str.append("^");

    // add string to error message stack
    errMsgStack.push(str.toString());
  }

  /**
   * Constructs an error message, appending it to the stderr buffer.
   *
   * @param tok The offending token. Includes location information where
   * the error occurred.
   * @param template An error message in the form of a format string.
   * @param args The strings that replace the format specifies
   * within the format string.
   * @throws SyntaxError
   */
  public void throwSyntaxError(Token tok, String template, Object... args)
    throws SyntaxError
  {
    error(tok, template, args);
    throw new SyntaxError("");
  }

  /**
   * Pops an error message off the stack.
   */
  public void pop()
  {
    errMsgStack.pop();
  }

  /**
   * Returns the error messages that have been reported.
   */
  @Override
  public String toString()
  {
    final StringBuilder result = new StringBuilder();

    if (errMsgStack.size() > 0)
    {
      for (String str : errMsgStack)
        result.append("\n").append(str);
    }

    final int numErrors = errMsgStack.size();

    // print number of errors after error messages
    result.append("\n")
      .append("\n")
      .append(numErrors)
      .append(" ")
      .append("Error")
      .append(numErrors == 1 ? "" : "s");

    return result.toString();
  }

}
