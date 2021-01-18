package me.mtk.torrey.Lexer;

/**
 * An implementation of a LL(1) recursive-descent
 * lexer that derives a stream of tokens from a
 * character stream by recognizing lexical patterns.
 * 
 * The produced token stream is then used by the parser
 * to produce an abstract syntax tree (AST) to represent
 * the syntactic structure of the input program in memory.
 */
public final class TorreyLexer extends BaseLexer
{

    /**
     * Construct a new lexer from an input program.
     * 
     * @param input An input program.
     */
    public TorreyLexer(final String input)
    {
        super(input);
    }

    /**
     * Examines the character under the cursor and
     * routes control flow to the appropriate subroutine
     * that subsequently produces a token based on a 
     * pattern match.
     */
    public void nextToken()
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

            case '(': addOneCharToken(TokenType.LPAREN); break;
            case ')': addOneCharToken(TokenType.RPAREN); break;

            case '+': addOneCharToken(TokenType.PLUS); break;
            case '-': addOneCharToken(TokenType.MINUS); break;
            case '*': addOneCharToken(TokenType.STAR); break;
            case '/': addOneCharToken(TokenType.SLASH); break;

            default:
                if (isDigit(currentChar))
                    consumeInteger();
                else if (isStartOfIdentifier(currentChar))
                    consumeIdentifier();
                else
                {
                    hasLexicalError = true;
                    addOneCharToken(TokenType.UNIDENTIFIED); 
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
    private void consumeInteger()
    {
        while (isDigit(peek())) nextChar();
        final int len = cursor - tokenIndexStart;
        addToken(TokenType.INTEGER, len);
    }

    /*
     * Adds an identifier or unidentified token to the collection.
     */
    private void consumeIdentifier()
    {
        // Consume the identifier.
        while (isPartOfIdentifier(peek())) nextChar();

        tokenIndexEnd = cursor;
        
        // First check if the identifier is a reserved
        // word in the language.
        final String identifier = input.substring(tokenIndexStart, 
            tokenIndexEnd);
        TokenType type = Keywords.get(identifier);

        addToken(type == null ? TokenType.UNIDENTIFIED : type);
    }

    /*
    * Indicates if the provided character is the start of
    * an identifier.
    * 
    * @param c A character from the source program.
    * @return True if the character is the start of an identifer;
    * False otherwise.
    */
    private boolean isStartOfIdentifier(char c)
    {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') ||
            (c == '_') || (c == '$');  
    }

    /*
    * Indicates if the provided character is part
    * of an identifier.
    * 
    * @param c A character from the source program.
    * @return True if the character is part of an identifier;
    * False otherwise.
    */
    private boolean isPartOfIdentifier(char c)
    {
        return isStartOfIdentifier(c) || isDigit(c) || c == '?' || c == '-';
    }
}
