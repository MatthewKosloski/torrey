package me.mtk.torrey.Parser;

import java.util.List;
import me.mtk.torrey.Lexer.*;

/**
 * Implements a LL(k) recursive-descent parser. Holds the
 * collection of tokens retrieved from the lexer and provides
 * an API for lookahead and matching against tokens.
 */
public abstract class Parser 
{
    // Holds the collection of tokens produced by a lexer.
    private final List<Token> tokens;

    // The current position in the token collection.
    private int cursor;

    /**
     * Constructs a new Parser object, initializing
     * it with a list of tokens.
     * 
     * @param tokens A list of tokens.
     */
    public Parser(List<Token> tokens)
    {
        this.tokens = tokens;
    }

    /**
     * The top-level rule of the grammar.
     */
    public abstract void program();

    /**
     * Consumes a token and moves the cursor forward
     * if the token under the cursor is of the specified
     * type. If it is not, an exception is thrown.
     *  
     * @param type The type of the token that we expect
     * the currently selected token in the buffer to be.
     */
    public void match(TokenType type)
    {
        if (peek().type().equals(type))
        {
            nextToken();
        }
        else
        {
            String err = String.format("Expected token %s but found %s", 
                type, peek().type());
            throw new Error(err);
        }
    }

    /**
     * Returns the first token of lookahead.
     * 
     * @return The second token of lookahead or null
     * if there are no more tokens to peek.
     */
    protected Token peek()
    {
        return lookahead(1);
    }

    /**
     * Returns the second token of lookahead.
     * 
     * @return The second token of lookahead or null
     * if there are no more tokens to peek.
     */
    protected Token peekNext()
    {
        return lookahead(2);
    }

    /**
     * Returns the next token in the buffer and
     * moves the cursor forward.
     * 
     * @return The next token in the buffer or null
     * if there are no more tokens in the buffer.
     */
    protected Token nextToken()
    {
        Token tok;

        try
        {
            tok = tokens.get(cursor++);
        }
        catch (IndexOutOfBoundsException e)
        {
            tok = null;
        }

        return tok;
    }

    /*
     * Returns the nth token of lookahead or null
     * if there is no nth token of lookahead.
     * 
     * @param n A non-negative integer less than the number
     * of tokens in the buffer.
     * @return The nth token of lookahead or null
     * if there is no nth token of lookahead.
     */
    private Token lookahead(int n)
    {
        Token tok;

        try
        {
            tok = tokens.get(cursor + n + 1);
        }
        catch (IndexOutOfBoundsException e)
        {
            tok = null;
        }

        return tok;
    }
}
