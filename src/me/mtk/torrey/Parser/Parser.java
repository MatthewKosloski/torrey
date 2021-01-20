package me.mtk.torrey.Parser;

import java.util.Collection;
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
    public abstract Collection<?> program();

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
    public Token peek()
    {
        return lookahead(1);
    }

    /**
     * Indicates whether the first token of lookahead
     * is of the specified type.
     * 
     * @param type A token type.
     * @return True if the TokenType of the first token
     * of lookahead is the specified type; False otherwise.
     */
    public boolean peek(TokenType type)
    {
        return peek().type() == type;
    }

    /**
     * Indicates whether the type of the first token of
     * lookahead is one of the specified token types.
     * 
     * @param types One or more token types.
     * @return True if at least one of the specified token
     * types is equal to the token type of the first token
     * of lookahead; False otherwise.
     */
    public boolean peek(TokenType... types)
    {
        for (TokenType type : types)
            if (peek(type)) return true;
        
        return false;
    }

    /**
     * Returns the second token of lookahead.
     * 
     * @return The second token of lookahead or null
     * if there are no more tokens to peek.
     */
    public Token peekNext()
    {
        return lookahead(2);
    }

    /**
     * Indicates whether the second token of lookahead
     * is of the specified type.
     * 
     * @param type A token type.
     * @return True if the TokenType of the second token
     * of lookahead is the specified type; False otherwise.
     */
    public boolean peekNext(TokenType type)
    {
        return peekNext().type() == type;
    }

    /**
     * Indicates whether the type of the second token of
     * lookahead is one of the specified token types.
     * 
     * @param types One or more token types.
     * @return True if at least one of the specified token
     * types is equal to the token type of the second token
     * of lookahead; False otherwise.
     */
    public boolean peekNext(TokenType... types)
    {
        for (TokenType type : types)
            if (peekNext(type)) return true;
        
        return false;
    }

    /**
     * Returns the next token in the buffer and
     * moves the cursor forward.
     * 
     * @return The next token in the buffer or null
     * if there are no more tokens in the buffer.
     */
    public Token nextToken()
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

    /**
     * Indicates whether there are still tokens in the buffer
     * that have yet to be processed.
     * 
     * @return True if there are remaining tokens; False otherwise.
     */
    public boolean hasTokens()
    {
        return !peek(TokenType.EOF);
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
            // conform to the convention that
            // the first lookahead token is the
            // one under the cursor
            tok = tokens.get(cursor + (n - 1));
        }
        catch (IndexOutOfBoundsException e)
        {
            tok = null;
        }

        return tok;
    }
}
