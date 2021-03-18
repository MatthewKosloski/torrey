package me.mtk.torrey.frontend.lexer;

import java.util.List;

/**
 * Stores a list of tokens and a toString
 * representation suitable for stdout.
 */
public final class TokenList 
{
    private List<Token> tokens;

    /**
     * Instantiates a new TokenList object,
     * encapsulating a list of tokens and
     * a toString representation.
     * 
     * @param tokens The list of tokens.
     */
    public TokenList(List<Token> tokens)
    {
        this.tokens = tokens;
    }

    /**
     * Returns the list of tokens in this list.
     * 
     * @return A list of token objects.
     */
    public List<Token> tokens()
    {
        return tokens;
    }

    /**
     * The toString representation of this TokenList.
     * 
     * @return A String object.
     */
    public String toString()
    {
        final StringBuilder sb = new StringBuilder();

        for (int i = 0; i < tokens.size(); i++)
            sb.append(tokens.get(i).toString())
                .append(i == tokens.size() - 1 ? "" : "\n");
            
        return sb.toString();
    }

}
