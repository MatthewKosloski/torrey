package me.mtk.torrey.error_reporter;

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
    public static final String ExpectedExpr = "Expected an integer, unary, binary, "
        + "print, let, or identifier expression but found '%s' instead";
    public static final String ExpectedUnaryBinaryPrint = "Expected a unary, binary, "
        + "or print expression but found '%s' instead";
    public static final String ExpectedOpeningParen = "Expected an opening parenthesis '('";
    public static final String ExpectedClosingParen = "Expected a closing parenthesis ')'";
    public static final String ExpectedOpeningBracket = "Expected an opening bracket '['";
    public static final String ExpectedClosingBracket = "Expected a closing bracket ']'";
    public static final String UnexpectedOperand = "Expected operand to operator '%s' to be " 
        + "type '%s' but found type '%s' instead";
    public static final String DivisionByZero = "Division by zero";
    public static final String ExpectedIdentifier = "Expected an identifier"
        + " but found '%s' instead";
}