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
    protected final List<Token> tokens;

    // The current index into the input program.
    protected int cursor;

    // The number of the line currently being processed.
    protected int curLine;

    // The index (into the input) of the first character 
    // of the line currently being processed.
    protected int curLineStart;

    // The number of the column of the line currently being processed.
    protected int curCol;

    // The index (into the input) of the first character 
    // of the token currently being constructed. 
    protected int tokenIndexStart;

    // The index (into the input) of the last character 
    // of the token currently being constructed.
    protected int tokenIndexEnd;

    // The start position of the token.
    protected int tokenStartLine;
    protected int tokenStartCol;

    // The end position of the token.
    protected int tokenEndLine;
    protected int tokenEndCol;

    // True if we encounter at least one unidentified
    // token; False otherwise.
    protected boolean hasLexicalError;

    // The input program from which we will extract tokens.
    protected final String input;

    /**
     * Constructs a new base lexer with the given input buffer and
     * an empty token list.
     * 
     * @param input The input program that is to be lex'd.
     */
    public BaseLexer(final String input)
    {
        this.input = input;
        tokens = new ArrayList<>();
        curLine = 1;
        curCol = 1;
    }

    /**
     * Examines the character under the cursor and
     * routes control flow to the appropriate subroutine
     * that subsequently produces a token based on a 
     * pattern match.
     */
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
        addToken(TokenType.EOF);

        return tokens;
    }

    /**
     * Adds a token to the collection with the specified
     * token type.
     * 
     * @param type The TokenType of the token.
     */
    public void addToken(final TokenType type)
    {
        tokenIndexEnd = cursor;
        tokenEndLine = curLine;
        tokenEndCol = curCol;

        // The rawText of the token is either the null
        // character "\0" or a substring of the input 
        // bounded by [tokenIndexStart, tokenIndexEnd).
        final String text = tokenIndexEnd > input.length()
            ? "\0"
            : input.substring(tokenIndexStart, tokenIndexEnd);

        // The line number and column number of the first
        // character of the token.
        final Position startPos = new Position(tokenStartLine, 
            tokenStartCol);

        // The line number and column number of the last
        // character of the token.
        final Position endPos = new Position(tokenEndLine,
            tokenEndCol);
            
        tokens.add(new Token(type, text, tokenIndexStart, tokenIndexEnd, 
            startPos, endPos));
    }

    /**
     * Moves the cursor over the next character, returning
     * the next character and updating relevant state properties.
     * 
     * @return The next character in the input buffer.
     */
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

    /**
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

    /**
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