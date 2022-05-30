package me.mtk.torrey.frontend.ir.instructions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import me.mtk.torrey.frontend.ir.addressing.IRConstAddress;
import me.mtk.torrey.frontend.ir.addressing.IRTempAddress;
import me.mtk.torrey.frontend.lexer.TokenType;
import me.mtk.torrey.frontend.ir.instructions.Quadruple.OpType;

public class IRBinaryInstTest
{
  private static final int EXPECTED_NUMBER_OF_TOKEN_TYPES = 26;
  private static final IRConstAddress IR_CONST_ADDR_1 = new IRConstAddress(10);
  private static final IRConstAddress IR_CONST_ADDR_2 = new IRConstAddress(32);
  private static final IRTempAddress IR_TEMP_ADDR = new IRTempAddress("t0");

  public static Stream<Arguments> tokenTypeToOpTypeMappings()
  {
    return Stream.of(
      arguments(TokenType.PLUS, OpType.ADD),
      arguments(TokenType.MINUS, OpType.SUB),
      arguments(TokenType.STAR, OpType.MULT),
      arguments(TokenType.SLASH, OpType.DIV),
      arguments(TokenType.EQUAL, OpType.EQUAL),
      arguments(TokenType.NOT, OpType.NEQUAL),
      arguments(TokenType.LT, OpType.LT),
      arguments(TokenType.LTE, OpType.LTE),
      arguments(TokenType.GT, OpType.GT),
      arguments(TokenType.GTE, OpType.GTE));
  }

  public static Stream<Arguments> tokenTypesThatDoNotMapToAnOpType()
  {
    return Stream.of(
      arguments(TokenType.LPAREN),
      arguments(TokenType.RPAREN),
      arguments(TokenType.LBRACK),
      arguments(TokenType.RBRACK),
      arguments(TokenType.PRINT),
      arguments(TokenType.PRINTLN),
      arguments(TokenType.LET),
      arguments(TokenType.AND),
      arguments(TokenType.OR),
      arguments(TokenType.IF),
      arguments(TokenType.TRUE),
      arguments(TokenType.FALSE),
      arguments(TokenType.INTEGER),
      arguments(TokenType.IDENTIFIER),
      arguments(TokenType.UNIDENTIFIED),
      arguments(TokenType.EOF));
  }

  @Test
  public void constructor_withResult_setsProperties()
  {
    IRBinaryInst actual = new IRBinaryInst(
      TokenType.PLUS,
      IR_CONST_ADDR_1,
      IR_CONST_ADDR_2,
      IR_TEMP_ADDR);

    assertEquals(OpType.ADD, actual.opType);
    assertSame(IR_CONST_ADDR_1, actual.arg1);
    assertSame(IR_CONST_ADDR_2, actual.arg2);
    assertSame(IR_TEMP_ADDR, actual.result);
  }

  @ParameterizedTest
  @MethodSource("tokenTypeToOpTypeMappings")
  public void constructor_tokenTypeMapsToOpType_mapsTokenTypeToOpType(TokenType tokType, OpType opType)
  {
    IRBinaryInst actual = new IRBinaryInst(
      tokType,
      IR_CONST_ADDR_1,
      IR_CONST_ADDR_2,
      IR_TEMP_ADDR);

    assertEquals(opType, actual.opType);
    assertEquals(EXPECTED_NUMBER_OF_TOKEN_TYPES, TokenType.values().length);
  }

  @ParameterizedTest
  @MethodSource("tokenTypesThatDoNotMapToAnOpType")
  public void constructor_tokenTypeDoesNotMapToOpType_throwsError(TokenType tokType)
  {
    Error thrown = assertThrows(
      Error.class,
      () -> new IRBinaryInst(
        tokType,
        IR_CONST_ADDR_1,
        IR_CONST_ADDR_2,
        IR_TEMP_ADDR));

    assertEquals("Unexpected token type " + tokType.toString(), thrown.getMessage());
    assertEquals(EXPECTED_NUMBER_OF_TOKEN_TYPES, TokenType.values().length);
  }

  @Test
  public void toString_normalScenario_returnsStringRepresentation()
  {
    IRBinaryInst actual = new IRBinaryInst(
      TokenType.PLUS,
      IR_CONST_ADDR_1,
      IR_CONST_ADDR_2,
      IR_TEMP_ADDR);

    assertEquals("t0 = 10 + 32", actual.toString());
  }
}
