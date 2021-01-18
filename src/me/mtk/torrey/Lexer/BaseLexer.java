package me.mtk.torrey.Lexer;

import java.util.List;
import java.util.ArrayList;

/**
 * This base class keeps track of the lexer's state
 * and provides support methods to interact with
 * the state. To implement a LL(1) recursive-descent
 * lexer, extend this class and implement methods to
 * recognize lexical patterns.
 */
public abstract class BaseLexer
{
    // Stores the tokens that are to be emitted.
    public final List<Token> tokens;

    // The current index into the input program.
    public int cursor;

    // The number of the line currently being processed.
    public int curLine;

    // The index (into the input) of the first character 
    // of the line currently being processed.
    public int curLineStart;

    // The number of the column of the line currently being processed.
    public int curCol;

    // The index (into the input) of the first character 
    // of the token currently being constructed. 
    public int tokenIndexStart;

    // The index (into the input) of the last character 
    // of the token currently being constructed.
    public int tokenIndexEnd;

    // The start position of the token.
    public int tokenStartLine;
    public int tokenStartCol;

    // The end position of the token.
    public int tokenEndLine;
    public int tokenEndCol;

    // True if we encounter at least one unidentified
    // token; False otherwise.
    public boolean hasLexicalError;

    // The input program from which we will extract tokens.
    public final String input;

    public BaseLexer(final String input)
    {
        this.input = input;
        tokens = new ArrayList<>();
        curLine = 1;
        curCol = 1;
    }

    public abstract void nextToken();

    /**
     * Starts the lexical analysis process, returning
     * the stream of tokens that have been extracted
     * from the input program.
     * 
     * @return The token stream.
     */
    public List<Token> start()
    {
        while (!isEOF())
        {
            tokenIndexStart = cursor;
            tokenStartCol = curCol;
            tokenStartLine = curLine;
            nextToken();
        }

        tokenIndexStart++;
        tokenStartCol++;
        curCol++;
        addToken(TokenType.EOF, 1);

        return tokens;
    }

    public void addOneCharToken(final TokenType type)
    {
        addToken(type, 1);
    }
    
    public void addToken(final TokenType type, final int length)
    {
        tokenIndexEnd = tokenIndexStart + length;
        addToken(type);
    }

    public void addToken(final TokenType type)
    {
        tokenEndLine = curLine;
        tokenEndCol = curCol;
        
        // The token's start and end indices into the input.
        final int start = tokenIndexStart;
        final int end = tokenIndexEnd;

        // The start and end indices can be used together to
        // extract the token's text from the input.
        String text;

        if (end > input.length())
            // end of file
            text = "\0";
        else
            text = input.substring(start, end);

        // The line number and column number of the first
        // character of the token.
        final Position startPos = new Position(tokenStartLine, 
            tokenStartCol);

        // The line number and column number of the last
        // character of the token.
        final Position endPos = new Position(tokenEndLine,
            tokenEndCol);
            
        tokens.add(new Token(type, text, start, end, startPos, endPos));
    }

    protected char nextChar()
    {
        final char nextChar = input.charAt(cursor++);

        if (nextChar == '\n')
        {
            curLine++;
            curCol = 1;
            curLineStart = cursor;
        }
        else
        {
            curCol++;
        }

        return nextChar;
    }

    /*
     * Returns the next character to be scanned (one character
     * of lookahead).
     * 
     * @return The first character of lookahead. 
     */
    protected char peek()
    {
        // There is no next character, so return 
        // the null character.
        if (isEOF()) return '\0';

        char peek = input.charAt(cursor);
        
        return peek;
    }

    /*
     * Indicates if the provided character is a digit as
     * specified by the regular expression [0-9].
     * 
     * @param c A character
     * @return True if c is a digit; False otherwise.
     */
    protected boolean isDigit(char c)
    {
        return c >= '0' && c <= '9';
    }

    /**
     * Indicates if the provided character is a whitespace
     * character.
     * 
     * @param c A character.
     * @return True if c is a whitespace character; false otherwise.
     */
    protected boolean isWhitespace(char c)
    {
        return c == ' ' || c == '\t' || c == '\n' || c == '\r';
    }

    /**
     * Indicates whether we've reached the end
     * of the file (EOF).
     * 
     * @return True if we're at EOF; False otherwise.
     */
    protected boolean isEOF()
    {
        return cursor >= input.length();
    }
}
