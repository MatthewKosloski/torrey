package me.mtk.torrey.IR;

/**
 * Indicates that the mode of the given address is not what was expected.
 */
public class IllegalAddressingModeException extends RuntimeException
{
    IllegalAddressingModeException(String m) { super(m); }    
}
