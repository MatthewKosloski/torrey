package me.mtk.torrey.Parser;

import java.util.List;
import me.mtk.torrey.AST.Program;
import me.mtk.torrey.ErrorReporter.ErrorReporter;
import me.mtk.torrey.ErrorReporter.SyntaxError;
import me.mtk.torrey.Lexer.Token;
import me.mtk.torrey.Lexer.TokenType;

/**
 * Implements a LL(k) recursive-descent parser. Holds the
 * collection of tokens retrieved from the lexer and provides
 * an API for lookahead and matching against tokens. Any 
 * general-purpose (non-grammar-specific) data and methods
 * should go in here.
 */
public abstract class Parser 
{

    // Accumulates the error messages encountered
    // during the parse, which can then be accessed
    // after parsing.
    protected final ErrorReporter reporter;

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
    public Parser(ErrorReporter reporter, List<Token> tokens)
    {
        this.reporter = reporter;
        this.tokens = tokens;
    }

    /**
     * Initiates the parsing of the tokens. 
     * 
     * @return A Program ASTNode, which holds the entire
     * parsed AST.
     * @throws SyntaxError If the error reporter has errors to report.
     */
    public Program parse() throws SyntaxError
    {
        final Program p = program();
        reporter.report("Encountered one or more syntax errors during parse:");
        return p;
    }

    // Any class that extends from us must implement
    // this method, which returns the root ASTNode, Program.
    protected abstract Program program();

    /**
     * If the token under the cursor has the same type
     * as the specified token type, then consume the
     * token and return true. Otherwise, return false.
     *  
     * @param type A TokenType.
     * @return True if the type of the token under the 
     * cursor is the specified token type; False otherwise.
     */
    public boolean match(TokenType type)
    {        
        if (peek().type().equals(type))
        {
            nextToken();
            return true;
        }
        
        return false;
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
        final Token tok = lookahead(1);
        cursor++;
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
     * @param n An integer less than the number
     * of tokens in the buffer.
     * @return The nth token of lookahead or null
     * if there is no nth token of lookahead.
     */
    public Token lookahead(int n)
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

    /**
     * Implements panic-mode error recovery. Until there
     * are no more tokens to parse, keep discarding tokens
     * until the next token of lookahead is the start of
     * an expression. This enables the parser to continue
     * parsing, even if it encounters a syntax error.
     */
    public void synchronize()
    {
        while (hasTokens())
        {
            nextToken();

            if (peek().type() == TokenType.LPAREN)
                break;
        }
    }

}
