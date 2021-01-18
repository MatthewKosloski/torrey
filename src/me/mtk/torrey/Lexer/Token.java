package me.mtk.torrey.Lexer;

/**
 * Represents a unit of output from the Lexer. Holds
 * information about the token for parsing and error handling.
 */
public final class Token 
{
    // The token type.
    private final TokenType type;
    
    // The raw text from the input program.
    private final String rawText;

    // The index at which the raw text begins
    // in the input program string, inclusive.
    private final int beginIndex;

    // The index at which the raw text ends
    // in the input program string, exclusive.
    private final int endIndex;

    // The line number and column number
    // at which the raw text starts in 
    // the input program.
    private final Position startPos;

    // The line number and column number
    // at which the raw text ends in 
    // the input program.
    private final Position endPos;

    Token(TokenType type, String rawText, int beginIndex, int endIndex, 
        Position startPos, Position endPos)
    {
        this.type = type;
        this.rawText = rawText;
        this.beginIndex = beginIndex;
        this.endIndex = endIndex;
        this.startPos = startPos;
        this.endPos = endPos;
    }

    public TokenType type()
    {
        return type;
    }

    public String rawText()
    {
        return rawText;
    }

    public int beginIndex()
    {
        return beginIndex;
    }

    public int endIndex()
    {
        return endIndex;
    }

    public Position startPos()
    {
        return startPos;
    }

    public Position endPos()
    {
        return endPos;
    }

    public String toString()
    {
        final StringBuilder sb = new StringBuilder();

        sb.append("<");
        sb.append("'");
        sb.append(rawText);
        sb.append("'");
        sb.append(",");
        sb.append(type);
        sb.append(",");
        sb.append(startPos.line());
        sb.append(":");
        sb.append(startPos.col());
        sb.append(",");
        sb.append(endPos.line());
        sb.append(":");
        sb.append(endPos.col());
        sb.append(">");

        return sb.toString();
    }
}
