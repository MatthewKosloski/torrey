package me.mtk.torrey.frontend.ir.instructions;

import org.junit.Test;
import org.junit.Assert;

import me.mtk.torrey.frontend.ir.instructions.Quadruple.OpType;
import me.mtk.torrey.frontend.lexer.TokenType;

public class QuadrupleTest
{

  private static final int EXPECTED_NUMBER_OF_IR_OP_TYPES = 16;
  private static final int EXPECTED_NUMBER_OF_TOKEN_TYPES = 26;

  @Test
  public void opType_allOpTypes_mapToTerminalSymbols()
  {
    Assert.assertEquals("+", OpType.ADD.toString());
    Assert.assertEquals("-", OpType.SUB.toString());
    Assert.assertEquals("*", OpType.MULT.toString());
    Assert.assertEquals("/", OpType.DIV.toString());
    Assert.assertEquals("==", OpType.EQUAL.toString());
    Assert.assertEquals("!=", OpType.NEQUAL.toString());
    Assert.assertEquals("<", OpType.LT.toString());
    Assert.assertEquals("<=", OpType.LTE.toString());
    Assert.assertEquals(">", OpType.GT.toString());
    Assert.assertEquals(">=", OpType.GTE.toString());
    Assert.assertEquals("call", OpType.CALL.toString());
    Assert.assertEquals("=", OpType.COPY.toString());
    Assert.assertEquals("goto", OpType.GOTO.toString());
    Assert.assertEquals("label", OpType.LABEL.toString());
    Assert.assertEquals("param", OpType.PARAM.toString());
    Assert.assertEquals("-", OpType.MINUS.toString());

    Assert.assertEquals(EXPECTED_NUMBER_OF_IR_OP_TYPES, OpType.values().length);
  }

  @Test
  public void getBinaryOpTypeFromTokenType_allMappableTokenTypes_mapToBinaryOpTypes()
  {
    Assert.assertEquals(OpType.ADD, OpType.getBinaryOpTypeFromTokenType(TokenType.PLUS));
    Assert.assertEquals(OpType.SUB, OpType.getBinaryOpTypeFromTokenType(TokenType.MINUS));
    Assert.assertEquals(OpType.MULT, OpType.getBinaryOpTypeFromTokenType(TokenType.STAR));
    Assert.assertEquals(OpType.DIV, OpType.getBinaryOpTypeFromTokenType(TokenType.SLASH));
    Assert.assertEquals(OpType.EQUAL, OpType.getBinaryOpTypeFromTokenType(TokenType.EQUAL));
    Assert.assertEquals(OpType.NEQUAL, OpType.getBinaryOpTypeFromTokenType(TokenType.NOT));
    Assert.assertEquals(OpType.LT, OpType.getBinaryOpTypeFromTokenType(TokenType.LT));
    Assert.assertEquals(OpType.LTE, OpType.getBinaryOpTypeFromTokenType(TokenType.LTE));
    Assert.assertEquals(OpType.GT, OpType.getBinaryOpTypeFromTokenType(TokenType.GT));
    Assert.assertEquals(OpType.GTE, OpType.getBinaryOpTypeFromTokenType(TokenType.GTE));

    Assert.assertEquals(EXPECTED_NUMBER_OF_TOKEN_TYPES, TokenType.values().length);
  }

  public void getBinaryOpTypeFromTokenType_tokenTypeDoesNotMap_throwsError()
  {
    Error lparenThrown = Assert.assertThrows(
      Error.class,
      () -> OpType.getBinaryOpTypeFromTokenType(TokenType.LPAREN));

    Assert.assertEquals("Unexpected token type LPAREN", lparenThrown.getMessage());
  }
}
