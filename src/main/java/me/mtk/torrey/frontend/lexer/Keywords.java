package me.mtk.torrey.frontend.lexer;

import java.util.Map;
import java.util.HashMap;

/**
 * Stores the keywords of the language.
 */
public final class Keywords
{
  private static final Map<String, TokenType> store;

  static
  {
    store = new HashMap<>();
    store.put("print", TokenType.PRINT);
    store.put("println", TokenType.PRINTLN);
    store.put("let", TokenType.LET);
    store.put("not", TokenType.NOT);
    store.put("and", TokenType.AND);
    store.put("or", TokenType.OR);
    store.put("if", TokenType.IF);
    store.put("true", TokenType.TRUE);
    store.put("false", TokenType.FALSE);
  }

  /**
   * Indicates whether the provided string maps to
   * a keyword in the language.
   *
   * @param str A string.
   * @return True if there exists a keyword k in the language
   * for which str.equals(k) is true; false otherwise.
   */
  public static boolean isKeyword(String str)
  {
    return store.containsKey(str);
  }

  /**
   * If there exists a keyword k in the language for
   * which str.equals(k) is true, then the token type
   * of the keyword is returned. Else, null is returned.
   *
   * @param str A string.
   * @return The token type for str; else null.
   */
  public static TokenType get(String str)
  {
    return store.get(str);
  }
}
