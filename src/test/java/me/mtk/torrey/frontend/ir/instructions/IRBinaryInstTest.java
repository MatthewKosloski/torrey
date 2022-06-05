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
import me.mtk.torrey.frontend.ir.addressing.IRAddress;
import me.mtk.torrey.frontend.ir.addressing.IRConstAddress;
import me.mtk.torrey.frontend.ir.addressing.IRTempAddress;
import me.mtk.torrey.frontend.lexer.TokenType;
import me.mtk.torrey.frontend.ir.instructions.Quadruple.OpType;

public class IRBinaryInstTest
{
  private static final int EXPECTED_NUMBER_OF_TOKEN_TYPES = 26;
  private static final IRAddress IR_CONST_ADDR_1 = new IRConstAddress(10);
  private static final IRAddress IR_CONST_ADDR_2 = new IRConstAddress(32);
  private static final IRAddress IR_TEMP_ADDR = new IRTempAddress("t0");

  public static Stream<Arguments> tokenTypeToOpTypeMappings()
  {
    return QuadrupleTest.tokenTypeToOpTypeMappings();
  }

  public static Stream<Arguments> tokenTypesThatDoNotMapToAnOpType()
  {
    return QuadrupleTest.tokenTypesThatDoNotMapToAnOpType();
  }

  public static Stream<Arguments> nullArgumentCombos()
  {
    return Stream.of(
      arguments(null, IR_CONST_ADDR_1, IR_CONST_ADDR_2, IR_TEMP_ADDR),
      arguments(TokenType.PLUS, null, IR_CONST_ADDR_2, IR_TEMP_ADDR),
      arguments(TokenType.PLUS, IR_CONST_ADDR_1, null, IR_TEMP_ADDR),
      arguments(TokenType.PLUS, IR_CONST_ADDR_1, IR_CONST_ADDR_2, null),
      arguments(null, null, null, null));
  }

  @Test
  public void constructor_normalScenario_setsProperties()
  {
    Quadruple actual = new IRBinaryInst(
      TokenType.PLUS,
      IR_CONST_ADDR_1,
      IR_CONST_ADDR_2,
      IR_TEMP_ADDR);

    assertEquals(OpType.ADD, actual.opType);
    assertSame(IR_CONST_ADDR_1, actual.arg1);
    assertSame(IR_CONST_ADDR_2, actual.arg2);
    assertSame(IR_TEMP_ADDR, actual.result);
  }

  @Test
  public void constructor_resultIsIncorrectType_throwsIllegalArgumentException()
  {
    assertThrows(
      IllegalArgumentException.class,
      () -> new IRBinaryInst(
        TokenType.PLUS,
        IR_CONST_ADDR_1,
        IR_CONST_ADDR_2,
        new IRConstAddress(1)));
  }

  @ParameterizedTest
  @MethodSource("nullArgumentCombos")
  public void constructor_nullArguments_throwsNullPointerException(
    TokenType tokType,
    IRAddress arg1,
    IRAddress arg2,
    IRAddress result
  )
  {
    assertThrows(
      NullPointerException.class,
      () -> new IRBinaryInst(tokType, arg1, arg2, result));
  }

  @ParameterizedTest
  @MethodSource("tokenTypeToOpTypeMappings")
  public void constructor_tokenTypeMapsToOpType_mapsTokenTypeToOpType(TokenType tokType, OpType opType)
  {
    Quadruple actual = new IRBinaryInst(
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
    Quadruple actual = new IRBinaryInst(
      TokenType.PLUS,
      IR_CONST_ADDR_1,
      IR_CONST_ADDR_2,
      IR_TEMP_ADDR);

    assertEquals("t0 = 10 + 32", actual.toString());
  }
}
