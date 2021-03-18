package me.mtk.torrey.lexer;

import me.mtk.torrey.error_reporter.ErrorMessages;
import me.mtk.torrey.error_reporter.ErrorReporter;
import me.mtk.torrey.error_reporter.SyntaxError;

/**
 * An implementation of a LL(1) recursive-descent
 * lexer that derives a stream of tokens from a
 * character stream by recognizing lexical patterns.
 * 
 * The produced token stream is then used by the parser
 * to produce an abstract syntax tree (AST) to represent
 * the syntactic structure of the input program in memory.
 */
public final class Lexer extends BaseLexer
{

    /**
     * Construct a new lexer from an input program.
     * 
     * @param input An input program.
     */
    public Lexer(final ErrorReporter reporter, final String input)
    {
        super(reporter, input);
    }

    /**
     * Examines the character under the cursor and
     * routes control flow to the appropriate subroutine
     * that subsequently produces a token based on a 
     * pattern match.
     */
    public void nextToken() throws SyntaxError
    {
        final char currentChar = nextChar();
        switch (currentChar)
        {            
            case ' ': 
            case '\t':
            case '\n':
            case '\r':
                consumeWhitespace(); break;

            case ';': consumeComment(); break;

            case '(': addToken(TokenType.LPAREN); break;
            case ')': addToken(TokenType.RPAREN); break;
            case '[': addToken(TokenType.LBRACK); break;
            case ']': addToken(TokenType.RBRACK); break;

            case '+': addToken(TokenType.PLUS); break;
            case '-': addToken(TokenType.MINUS); break;
            case '*': addToken(TokenType.STAR); break;
            case '/': addToken(TokenType.SLASH); break;

            case '=':
                if (match('='))
                    addToken(TokenType.EQUAL);
                break;

            case '<': addToken(match('=') 
                ? TokenType.LTE : TokenType.LT); break;

            case '>': addToken(match('=') 
                ? TokenType.GTE : TokenType.GT); break;

            default:
                if (isDigit(currentChar))
                    addIntegerToken();
                else if (isIdInitial(currentChar))
                    addIdentifierToken();
                else
                {
                    hasLexicalError = true;
                    addToken(TokenType.UNIDENTIFIED);
                    reporter.error(getLastToken(), 
                        ErrorMessages.UnexpectedCharacter, 
                        getLastToken().rawText());
                }

                break;
        }
    }

    /*
     * Consumes any substring of one or more white space characters.
     */
    private void consumeWhitespace()
    {
        while (isWhitespace(peek())) nextChar();
    }

    /*
     * Assumes the previous character was the start of an in-line comment
     * and consumes any characters up to the newline character.
     */
    private void consumeComment()
    {
        while (peek() != '\n' && !isEOF()) nextChar();
    }

    /*
     * Adds an integer token to the collection, matching against [0-9]+.
     */
    private void addIntegerToken()
    {
        while (isDigit(peek())) nextChar();
        addToken(TokenType.INTEGER);
    }

    /*
     * Adds an identifier or unidentified token to the collection.
     */
    private void addIdentifierToken()
    {
        // Consume the identifier.
        while (isIdSubsequent(peek())) nextChar();

        tokenIndexEnd = cursor;
        
        // First check if the identifier is a reserved
        // word in the language.
        final String identifier = input.substring(tokenIndexStart, 
            tokenIndexEnd);
        TokenType type = Keywords.get(identifier);

        addToken(type == null ? TokenType.IDENTIFIER : type);
    }

    /*
    * Indicates if the provided character is the start of
    * an identifier.
    * 
    * @param c A character from the source program.
    * @return True if the character is the start of an identifer;
    * False otherwise.
    */
    private boolean isIdInitial(char c)
    {
        // [a-zA-Z_$]
        return isLetter(c) || c == '_' || c == '$';  
    }

    /*
    * Indicates if the provided character is part
    * of an identifier.
    * 
    * @param c A character from the source program.
    * @return True if the character is part of an identifier;
    * False otherwise.
    */
    private boolean isIdSubsequent(char c)
    {
        // [a-zA-Z0-9_$!?-]
        return isIdInitial(c) || isDigit(c) || c == '!' 
            || c == '?' || c == '-';
    }
}
