package me.mtk.torrey.Parser;

/**
 * Stores the format strings used for error 
 * messages by the parser.
 * 
 * Inspired by the error handling that is used
 * in the Babel transpiler:
 * 
 * See: https://github.com/babel/babel/blob/main/packages/babel-parser/src/parser/error-message.js
 */
public final class ErrorMessages 
{
    public static final String UnexpectedCharacter = "Unexpected character '%s'";
    public static final String ExpectedButFound = "Expected '%s' but found '%s'";
    public static final String ExpectedExpressionButFound = "Expected an integer, unary, binary, "
    + "or print expression but found '%s' instead";
    public static final String ExpectedOpeningParen = "Expected an opening parenthesis '('";
    public static final String ExpectedClosingParen = "Expected a closing parenthesis ')'";
}