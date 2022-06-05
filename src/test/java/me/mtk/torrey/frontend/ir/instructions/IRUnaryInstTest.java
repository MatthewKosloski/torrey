package me.mtk.torrey.frontend.ir.instructions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import me.mtk.torrey.frontend.ir.addressing.IRAddress;
import me.mtk.torrey.frontend.ir.addressing.IRConstAddress;
import me.mtk.torrey.frontend.ir.addressing.IRTempAddress;
import me.mtk.torrey.frontend.lexer.TokenType;
import me.mtk.torrey.frontend.ir.instructions.Quadruple.OpType;

public class IRUnaryInstTest
{
  private static final int EXPECTED_NUMBER_OF_TOKEN_TYPES = 26;
  private static final IRAddress IR_CONST_ADDR = new IRConstAddress(10);
  private static final IRAddress IR_TEMP_ADDR = new IRTempAddress("t0");

  public static Stream<Arguments> unaryTokenTypeToOpTypeMappings()
  {
    return QuadrupleTest.unaryTokenTypeToOpTypeMappings();
  }

  public static Stream<Arguments> nullArgumentCombos()
  {
    return Stream.of(
      arguments(null, null, null),
      arguments(null, IR_CONST_ADDR, IR_TEMP_ADDR),
      arguments(TokenType.MINUS, null, IR_TEMP_ADDR),
      arguments(TokenType.MINUS, IR_CONST_ADDR, null));
  }

  @Test
  public void constructor_normalScenario_setsProperties()
  {
    Quadruple actual = new IRUnaryInst(
      TokenType.MINUS,
      IR_CONST_ADDR,
      IR_TEMP_ADDR);

    assertEquals(OpType.MINUS, actual.opType);
    assertSame(IR_CONST_ADDR, actual.arg1);
    assertNull(actual.arg2);
    assertSame(IR_TEMP_ADDR, actual.result);
  }

  @Test
  public void constructor_resultIsIncorrectType_throwsIllegalArgumentException()
  {
    assertThrows(
      IllegalArgumentException.class,
      () -> new IRUnaryInst(
        TokenType.MINUS,
        IR_CONST_ADDR,
        IR_CONST_ADDR));
  }

  @ParameterizedTest
  @MethodSource("nullArgumentCombos")
  public void constructor_nullArguments_throwsNullPointerException(
    TokenType tokType,
    IRAddress arg,
    IRAddress result
  )
  {
    assertThrows(
      NullPointerException.class,
      () -> new IRUnaryInst(tokType, arg, result));
  }

  @ParameterizedTest
  @MethodSource("unaryTokenTypeToOpTypeMappings")
  public void constructor_tokenTypeMapsToOpType_mapsTokenTypeToOpType(TokenType tokType, OpType opType)
  {
    Quadruple actual = new IRUnaryInst(
      tokType,
      IR_CONST_ADDR,
      IR_TEMP_ADDR);

    assertEquals(opType, actual.opType);
    assertEquals(EXPECTED_NUMBER_OF_TOKEN_TYPES, TokenType.values().length);
  }

  @ParameterizedTest
  @EnumSource(mode = EnumSource.Mode.EXCLUDE, names = { "MINUS" })
  public void constructor_tokenTypeDoesNotMapToOpType_throwsError(TokenType tokType)
  {
    Error thrown = assertThrows(
      Error.class,
      () -> new IRUnaryInst(
        tokType,
        IR_CONST_ADDR,
        IR_TEMP_ADDR));

    assertEquals("Unexpected token type " + tokType.toString(), thrown.getMessage());
    assertEquals(EXPECTED_NUMBER_OF_TOKEN_TYPES, TokenType.values().length);
  }

  @Test
  public void toString_normalScenario_returnsStringRepresentation()
  {
    Quadruple actual = new IRUnaryInst(
      TokenType.MINUS,
      IR_CONST_ADDR,
      IR_TEMP_ADDR);

    assertEquals("t0 = - 10", actual.toString());
  }
}
