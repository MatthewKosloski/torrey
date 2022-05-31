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
import me.mtk.torrey.frontend.ir.addressing.IRLabelAddress;
import me.mtk.torrey.frontend.ir.instructions.Quadruple.OpType;
import me.mtk.torrey.frontend.lexer.TokenType;

public class IRIfInstTest
{
  private static final int EXPECTED_NUMBER_OF_TOKEN_TYPES = 26;
  private static final IRAddress IR_CONST_ADDR1 = new IRConstAddress(2);
  private static final IRAddress IR_CONST_ADDR2 = new IRConstAddress(3);
  private static final IRAddress IR_LABEL_ADDR = new IRLabelAddress("l0");

  public static Stream<Arguments> tokenTypeToInvertedOpTypeMappings()
  {
    return QuadrupleTest.tokenTypeToInvertedOpTypeMappings();
  }

  public static Stream<Arguments> tokenTypesThatDoNotMapToAnOpType()
  {
    return QuadrupleTest.tokenTypesThatDoNotMapToAnOpType();
  }

  public static Stream<Arguments> nullArgumentCombos()
  {
    return Stream.of(
      arguments(null, null, null, null),
      arguments(null, IR_CONST_ADDR1, IR_CONST_ADDR2, IR_LABEL_ADDR),
      arguments(TokenType.GT, null, IR_CONST_ADDR2, IR_LABEL_ADDR),
      arguments(TokenType.GT, IR_CONST_ADDR1, null, IR_LABEL_ADDR),
      arguments(TokenType.GT, IR_CONST_ADDR1, IR_CONST_ADDR2, null));
  }

  @Test
  public void constructor_normalScenario_setsProperties()
  {
    Quadruple actual = new IRIfInst(
      TokenType.GT,
      IR_CONST_ADDR1,
      IR_CONST_ADDR2,
      IR_LABEL_ADDR);

    assertEquals(OpType.LTE, actual.opType);
    assertSame(IR_CONST_ADDR1, actual.arg1);
    assertSame(IR_CONST_ADDR2, actual.arg2);
    assertSame(IR_LABEL_ADDR, actual.result);
  }

  @Test
  public void constructor_resultIsIncorrectType_throwsIllegalArgumentException()
  {
    assertThrows(
      IllegalArgumentException.class,
      () -> new IRIfInst(
        TokenType.GT,
        IR_CONST_ADDR1,
        IR_CONST_ADDR2,
        IR_CONST_ADDR2));
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
      () -> new IRIfInst(
        tokType,
        arg1,
        arg2,
        result));
  }

  @ParameterizedTest
  @MethodSource("tokenTypeToInvertedOpTypeMappings")
  public void constructor_tokenTypeMapsToInvertedOpType_mapsTokenTypeToOpType(TokenType tokType, OpType opType)
  {
    Quadruple actual = new IRIfInst(
      tokType,
      IR_CONST_ADDR1,
      IR_CONST_ADDR2,
      IR_LABEL_ADDR);

    assertEquals(opType, actual.opType);
    assertEquals(EXPECTED_NUMBER_OF_TOKEN_TYPES, TokenType.values().length);
  }

  @ParameterizedTest
  @MethodSource("tokenTypesThatDoNotMapToAnOpType")
  public void constructor_tokenTypeDoesNotMapToOpType_throwsError(TokenType tokType)
  {
    Error thrown = assertThrows(
      Error.class,
      () -> new IRIfInst(
        tokType,
        IR_CONST_ADDR1,
        IR_CONST_ADDR2,
        IR_LABEL_ADDR));

    assertEquals("Unexpected token type " + tokType.toString(), thrown.getMessage());
    assertEquals(EXPECTED_NUMBER_OF_TOKEN_TYPES, TokenType.values().length);
  }

  @Test
  public void toString_normalScenario_returnsStringRepresentation()
  {
    Quadruple actual = new IRIfInst(
      TokenType.GT,
      IR_CONST_ADDR1,
      IR_CONST_ADDR2,
      IR_LABEL_ADDR);

    assertEquals("if 2 <= 3 goto l0", actual.toString());
  }
}
