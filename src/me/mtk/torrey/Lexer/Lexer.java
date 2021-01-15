package me.mtk.torrey.Lexer;

import java.util.List;
import java.util.ArrayList;

/**
 * An implementation of a LL(1) recursive-descent
 * lexer that derives a stream of tokens from a
 * character stream by recognizing lexical patterns.
 * 
 * The produced token stream is then used by the parser
 * to produce an abstract syntax tree (AST) to represent
 * the syntactic structure of the input program in memory.
 */
public class Lexer 
{

    // Holds the ever-changing properties of
    // the lexer. Encapsulating these changing
    // properties in an object improves their
    // visibility when debugging.
    private final class State
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

        // the end position of the token.
        public int tokenEndLine;
        public int tokenEndCol;

        State()
        {
            this.tokens = new ArrayList<>();
        }

        public void addToken(final TokenType type)
        {
            // The token's start and end indices into the input.
            final int start = state.tokenIndexStart;
            final int end = state.tokenIndexEnd;

            // The start and end indices can be used together to
            // extract the token's text from the input.
            final String text = input.substring(start, end);

            // The line number and column number of the first
            // character of the token.
            final Position startPos = new Position(state.tokenStartLine, 
                state.tokenStartCol);

            // The line number and column number of the last
            // character of the token.
            final Position endPos = new Position(state.tokenEndLine,
                state.tokenEndCol);
                
            tokens.add(new Token(type, text, start, end, startPos, endPos));
        }
    }

    // The input program from which we will extract tokens.
    private final String input;

    // The lexer's state object, which holds line and column
    // info, current token info, accumulated token list, etc.
    private final State state;

    /**
     * Construct a new lexer from an input program.
     * 
     * @param input An input program.
     */
    Lexer(final String input)
    {
        this.input = input;
        state = new State();
        state.curLine = 1;
    }

    /**
     * Starts the lexical analysis process, returning
     * the stream of tokens that have been extracted
     * from the input program.
     * 
     * @return The token stream.
     */
    public List<Token> start()
    {

    }

    /**
     * Indicates whether we've reached the end
     * of the file.
     * 
     * @return True if we're at EOF; False otherwise.
     */
    private boolean isEOF()
    {
        return state.cursor >= input.length();
    }

}
