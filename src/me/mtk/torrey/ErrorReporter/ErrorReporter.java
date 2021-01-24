package me.mtk.torrey.ErrorReporter;

import me.mtk.torrey.Lexer.Token;

public class ErrorReporter 
{
 
    // The input program.
    private String input;

    // Accumulates the error messages.
    private StringBuilder errorBuilder;

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
        this.errorBuilder = new StringBuilder();
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
    public void report(String message) throws SyntaxError
    {
        if (errorBuilder.length() > 0)
        {
            // We have errors to report.
            System.err.println(message);
            throw new SyntaxError(this.toString());
        }
    }

    public void reportSemanticError(String message) throws SemanticError
    {
        if (errorBuilder.length() > 0)
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

        final String offendingLine = input.substring(tok.beginLineIndex(), endIndex);

        errorBuilder.append("\n")
            .append(String.format(template, args))
            .append(" ")
            // Print the line number and column number of the offending token
            .append(tok.startPos())
            .append("\n\n")
            .append(offendingLine)
            .append("\n");

        // Print a "^" character, pointing to the offending token.
        for (int i = 1; i < tok.endIndex() - tok.beginLineIndex(); i++)
            errorBuilder.append(" ");
        errorBuilder.append("^");
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
    public void throwError(Token tok, String template, Object... args) 
    throws SyntaxError
    {
        error(tok, template, args);
        throw new SyntaxError("");
    }

    /**
     * Returns the error messages that have been reported.
     */
    public String toString()
    {
        return errorBuilder.toString();
    }
    
}
