package me.mtk.torrey.frontend.lexer;

public enum TokenType
{
  LPAREN, RPAREN, LBRACK, RBRACK,
  PLUS, MINUS, STAR, SLASH,
  PRINT, PRINTLN, LET,
  NOT, AND, OR, IF,
  EQUAL, LT, LTE, GT, GTE,
  TRUE, FALSE,
  INTEGER, IDENTIFIER, UNIDENTIFIED, EOF
}
