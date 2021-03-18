package me.mtk.torrey.frontend.lexer;

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

    // The index into the input program at which
    // the first character of the token's start 
    // line is located.
    private final int beginLineIndex;

    // The line number and column number
    // at which the raw text starts in 
    // the input program.
    private final Position startPos;

    // The line number and column number
    // at which the raw text ends in 
    // the input program.
    private final Position endPos;

    public Token(TokenType type, String rawText, int beginIndex, int endIndex, 
        int beginLineIndex, Position startPos, Position endPos)
    {
        this.type = type;
        this.rawText = rawText;
        this.beginIndex = beginIndex;
        this.endIndex = endIndex;
        this.beginLineIndex = beginLineIndex;
        this.startPos = startPos;
        this.endPos = endPos;
    }

    /**
     * Constructs a token of the given type and raw text,
     * excluding any location information.
     * 
     * @param type
     * @param rawText
     */
    public Token(TokenType type, String rawText)
    {
        this(type, rawText, -1, -1, -1, null, null);
    }

    /**
     * Constructs an "empty" token with no location 
     * information or raw text.
     */
    public Token()
    {
        this(TokenType.UNIDENTIFIED, "");
    }

    /**
     * Indicates whether the type of this token is
     * one of the specified types.
     * 
     * @param types One or more token types.
     * @return True if the type is one of the given 
     * token types; False otherwise.
     */
    public boolean isType(TokenType ... types)
    {
        for (TokenType t : types)
        {
            if (t == type) 
                return true;
        }

        return false;
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

    public int beginLineIndex()
    {
        return beginLineIndex;
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
        sb.append(startPos != null ? startPos.line() : -1);
        sb.append(":");
        sb.append(startPos != null ? startPos.col() : -1);
        sb.append(",");
        sb.append(endPos != null ? endPos.line() : -1);
        sb.append(":");
        sb.append(endPos != null ? endPos.col() : -1);
        sb.append(">");

        return sb.toString();
    }
}
