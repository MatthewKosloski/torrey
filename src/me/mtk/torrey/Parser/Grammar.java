package me.mtk.torrey.Parser;

import java.util.List;
import me.mtk.torrey.Lexer.Token;

/**
 * Translates the context-free grammar to a 
 * LL(k) recursive-descent parser that matches 
 * phrases and sentences in the Torrey programming 
 * language specified by the grammar.
 * 
 * For each rule, r, defined in the context-free grammar,
 * we build a method of the same name. For rules with
 * alternatives, we use appropriate control flow.
 */
public class Grammar extends Parser
{
    public Grammar(List<Token> tokens)
    {
        super(tokens);
    }
}
