package me.mtk.torrey.frontend.ir.instructions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import me.mtk.torrey.frontend.ir.instructions.Quadruple.OpType;
import me.mtk.torrey.frontend.lexer.TokenType;

public class QuadrupleTest
{

  private static final int EXPECTED_NUMBER_OF_IR_OP_TYPES = 16;
  private static final int EXPECTED_NUMBER_OF_TOKEN_TYPES = 26;

  @Test
  public void opType_allOpTypes_mapToTerminalSymbols()
  {
    assertEquals("+", OpType.ADD.toString());
    assertEquals("-", OpType.SUB.toString());
    assertEquals("*", OpType.MULT.toString());
    assertEquals("/", OpType.DIV.toString());
    assertEquals("==", OpType.EQUAL.toString());
    assertEquals("!=", OpType.NEQUAL.toString());
    assertEquals("<", OpType.LT.toString());
    assertEquals("<=", OpType.LTE.toString());
    assertEquals(">", OpType.GT.toString());
    assertEquals(">=", OpType.GTE.toString());
    assertEquals("call", OpType.CALL.toString());
    assertEquals("=", OpType.COPY.toString());
    assertEquals("goto", OpType.GOTO.toString());
    assertEquals("label", OpType.LABEL.toString());
    assertEquals("param", OpType.PARAM.toString());
    assertEquals("-", OpType.MINUS.toString());

    assertEquals(EXPECTED_NUMBER_OF_IR_OP_TYPES, OpType.values().length);
  }

  @Test
  public void getBinaryOpTypeFromTokenType_allMappableTokenTypes_mapToBinaryOpTypes()
  {
    assertEquals(OpType.ADD, OpType.getBinaryOpTypeFromTokenType(TokenType.PLUS));
    assertEquals(OpType.SUB, OpType.getBinaryOpTypeFromTokenType(TokenType.MINUS));
    assertEquals(OpType.MULT, OpType.getBinaryOpTypeFromTokenType(TokenType.STAR));
    assertEquals(OpType.DIV, OpType.getBinaryOpTypeFromTokenType(TokenType.SLASH));
    assertEquals(OpType.EQUAL, OpType.getBinaryOpTypeFromTokenType(TokenType.EQUAL));
    assertEquals(OpType.NEQUAL, OpType.getBinaryOpTypeFromTokenType(TokenType.NOT));
    assertEquals(OpType.LT, OpType.getBinaryOpTypeFromTokenType(TokenType.LT));
    assertEquals(OpType.LTE, OpType.getBinaryOpTypeFromTokenType(TokenType.LTE));
    assertEquals(OpType.GT, OpType.getBinaryOpTypeFromTokenType(TokenType.GT));
    assertEquals(OpType.GTE, OpType.getBinaryOpTypeFromTokenType(TokenType.GTE));

    assertEquals(EXPECTED_NUMBER_OF_TOKEN_TYPES, TokenType.values().length);
  }

  public void getBinaryOpTypeFromTokenType_tokenTypeDoesNotMap_throwsError()
  {
    Error lparenThrown = assertThrows(
      Error.class,
      () -> OpType.getBinaryOpTypeFromTokenType(TokenType.LPAREN));

    assertEquals("Unexpected token type LPAREN", lparenThrown.getMessage());
  }
}
