package me.mtk.torrey.lexer;

public enum TokenType
{
    // Grouping tokens
    LPAREN, RPAREN,

    // Binary arithmetic tokens
    PLUS, MINUS, STAR, SLASH,

    // Print expression
    PRINT, PRINTLN,

    INTEGER,

    UNIDENTIFIED,

    // End of file token
    EOF
}