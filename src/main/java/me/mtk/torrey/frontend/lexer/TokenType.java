package me.mtk.torrey.frontend.lexer;

public enum TokenType
{
  LPAREN, RPAREN, LBRACK, RBRACK,
  PLUS, MINUS, STAR, SLASH,
  PRINT, PRINTLN,
  LET,
  EQUAL, NOT, AND, OR, IF,
  LT, LTE, GT, GTE,
  TRUE, FALSE,
  INTEGER,
  IDENTIFIER, UNIDENTIFIED, EOF
}
