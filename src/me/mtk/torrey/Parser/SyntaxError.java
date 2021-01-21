package me.mtk.torrey.Parser;

/**
 * Represents a syntax error that is thrown either during
 * lexical or syntax analysis.
 */

@SuppressWarnings("serial")
public class SyntaxError extends Exception
{
    public SyntaxError(String m) { super(m); }
}
