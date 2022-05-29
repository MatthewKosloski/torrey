package me.mtk.torrey.frontend.ir.instructions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import me.mtk.torrey.frontend.ir.addressing.IRAddress;
import me.mtk.torrey.frontend.ir.addressing.IRConstAddress;
import me.mtk.torrey.frontend.ir.addressing.IRLabelAddress;
import me.mtk.torrey.frontend.ir.addressing.IRTempAddress;
import me.mtk.torrey.frontend.ir.instructions.Quadruple.OpType;
import me.mtk.torrey.frontend.lexer.TokenType;

public class QuadrupleTest
{

  private static final int EXPECTED_NUMBER_OF_IR_OP_TYPES = 16;
  private static final int EXPECTED_NUMBER_OF_TOKEN_TYPES = 26;

  private final IRConstAddress testIRConstAddress1 = new IRConstAddress(10);
  private final IRConstAddress testIRConstAddress2 = new IRConstAddress(32);
  private final IRLabelAddress testIRLabelAddress = new IRLabelAddress("l0");
  private final IRTempAddress testIRTempAddress = new IRTempAddress("t0");

  class ConcreteImpl extends Quadruple
  {
    public ConcreteImpl(OpType opType, IRAddress arg1, IRAddress arg2, IRTempAddress result)
    {
      super(opType, arg1, arg2, result);
    }

    public ConcreteImpl(OpType opType, IRAddress arg1, IRAddress arg2, IRLabelAddress result)
    {
      super(opType, arg1, arg2, result);
    }

    public ConcreteImpl(OpType opType, IRAddress arg)
    {
      super(opType, arg);
    }
  }

  public static Stream<Arguments> provider1()
  {
    return Stream.of(
      arguments("+", OpType.ADD.toString()),
      arguments("-", OpType.SUB.toString()),
      arguments("*", OpType.MULT.toString()),
      arguments("/", OpType.DIV.toString()),
      arguments("==", OpType.EQUAL.toString()),
      arguments("!=", OpType.NEQUAL.toString()),
      arguments("<", OpType.LT.toString()),
      arguments("<=", OpType.LTE.toString()),
      arguments(">", OpType.GT.toString()),
      arguments(">=", OpType.GTE.toString()),
      arguments("call", OpType.CALL.toString()),
      arguments("=", OpType.COPY.toString()),
      arguments("goto", OpType.GOTO.toString()),
      arguments("label", OpType.LABEL.toString()),
      arguments("param", OpType.PARAM.toString()),
      arguments("-", OpType.MINUS.toString()));
  }

  @ParameterizedTest
  @MethodSource("provider1")
  public void opType_normalScenario_mapsToTerminalSymbol(String expected, String actual)
  {
    assertEquals(expected, actual);
    assertEquals(EXPECTED_NUMBER_OF_IR_OP_TYPES, OpType.values().length);
  }

  public static Stream<Arguments> provider2()
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

  @ParameterizedTest
  @MethodSource("provider2")
  public void getBinaryOpTypeFromTokenType_tokenTypeHasMap_mapsToOpType(TokenType tokType, OpType expected)
  {
    OpType actual = OpType.getBinaryOpTypeFromTokenType(tokType);

    assertEquals(expected, actual);
    assertEquals(EXPECTED_NUMBER_OF_TOKEN_TYPES, TokenType.values().length);
  }

  @ParameterizedTest
  @EnumSource(mode = EnumSource.Mode.INCLUDE, names = {
    "LPAREN", "RPAREN", "LBRACK", "RBRACK",
    "PRINT", "PRINTLN",
    "LET",
    "AND", "OR", "IF",
    "TRUE", "FALSE", "INTEGER",
    "IDENTIFIER", "UNIDENTIFIED", "EOF",
  })
  public void getBinaryOpTypeFromTokenType_tokenTypeDoesNotHaveMap_throwsError(TokenType tokType)
  {
    Error thrown = assertThrows(
      Error.class,
      () -> OpType.getBinaryOpTypeFromTokenType(tokType));

    assertEquals("Unexpected token type " + tokType.toString(), thrown.getMessage());
    assertEquals(EXPECTED_NUMBER_OF_TOKEN_TYPES, TokenType.values().length);
  }

  public static Stream<Arguments> provider3()
  {
    return Stream.of(arguments(TokenType.MINUS, OpType.MINUS));
  }

  @ParameterizedTest
  @MethodSource("provider3")
  public void getUnaryOpTypeFromTokenType_tokenTypeHasMap_mapsToOpType(TokenType tokType, OpType expected)
  {
    OpType actual = OpType.getUnaryOpTypeFromTokenType(tokType);

    assertEquals(expected, actual);
    assertEquals(EXPECTED_NUMBER_OF_TOKEN_TYPES, TokenType.values().length);
  }

  @ParameterizedTest
  @EnumSource(mode = EnumSource.Mode.EXCLUDE, names = { "MINUS" })
  public void getUnaryOpTypeFromTokenType_tokenTypeDoesNotHaveMap_throwsError(TokenType tokType)
  {
    Error thrown = assertThrows(
      Error.class,
      () -> OpType.getUnaryOpTypeFromTokenType(tokType));

    assertEquals("Unexpected token type " + tokType.toString(), thrown.getMessage());
    assertEquals(EXPECTED_NUMBER_OF_TOKEN_TYPES, TokenType.values().length);
  }


  public static Stream<Arguments> provider4()
  {
    return Stream.of(
      arguments(OpType.ADD, OpType.SUB),
      arguments(OpType.SUB, OpType.ADD),
      arguments(OpType.MULT, OpType.DIV),
      arguments(OpType.DIV, OpType.MULT),
      arguments(OpType.EQUAL, OpType.NEQUAL),
      arguments(OpType.NEQUAL, OpType.EQUAL),
      arguments(OpType.LT, OpType.GTE),
      arguments(OpType.LTE, OpType.GT),
      arguments(OpType.GT, OpType.LTE),
      arguments(OpType.GTE, OpType.LT));
  }

  @ParameterizedTest
  @MethodSource("provider4")
  public void getInvertedBinaryOpType_opTypeHasMap_mapsToInvertedOpType(OpType opType, OpType expected)
  {
    OpType actual = OpType.getInvertedBinaryOpType(opType);

    assertEquals(expected, actual);
    assertEquals(EXPECTED_NUMBER_OF_IR_OP_TYPES, OpType.values().length);
  }

  @ParameterizedTest
  @EnumSource(mode = EnumSource.Mode.INCLUDE, names = {
    "CALL",
    "COPY",
    "GOTO",
    "LABEL",
    "PARAM",
    "MINUS"
  })
  public void getInvertedBinaryOpType_opTypeDoesNotHaveMap_throwsError(OpType opType)
  {
    Error thrown = assertThrows(
      Error.class,
      () -> OpType.getInvertedBinaryOpType(opType));

    assertEquals("Unexpected op type " + opType.toString(), thrown.getMessage());
    assertEquals(EXPECTED_NUMBER_OF_IR_OP_TYPES, OpType.values().length);
  }

  public static Stream<Arguments> provider5()
  {
    return Stream.of(
      arguments(TokenType.PLUS, OpType.SUB),
      arguments(TokenType.MINUS, OpType.ADD),
      arguments(TokenType.STAR, OpType.DIV),
      arguments(TokenType.SLASH, OpType.MULT),
      arguments(TokenType.EQUAL, OpType.NEQUAL),
      arguments(TokenType.NOT, OpType.EQUAL),
      arguments(TokenType.LT, OpType.GTE),
      arguments(TokenType.LTE, OpType.GT),
      arguments(TokenType.GT, OpType.LTE),
      arguments(TokenType.GTE, OpType.LT));
  }

  @ParameterizedTest
  @MethodSource("provider5")
  public void getInvertedBinaryOpTypeFromTokenType_tokenTypeHasMap_mapsToInvertedOpType(TokenType tokenType, OpType expected)
  {
    OpType actual = OpType.getInvertedBinaryOpTypeFromTokenType(tokenType);

    assertEquals(expected, actual);
    assertEquals(EXPECTED_NUMBER_OF_TOKEN_TYPES, TokenType.values().length);
  }

  @ParameterizedTest
  @EnumSource(mode = EnumSource.Mode.INCLUDE, names = {
    "LPAREN", "RPAREN", "LBRACK", "RBRACK",
    "PRINT", "PRINTLN",
    "LET",
    "AND", "OR", "IF",
    "TRUE", "FALSE", "INTEGER",
    "IDENTIFIER", "UNIDENTIFIED", "EOF",
  })
  public void getInvertedBinaryOpTypeFromTokenType_tokenTypeDoesNotHaveMap_throwsError(TokenType tokenType)
  {
    Error thrown = assertThrows(
      Error.class,
      () -> OpType.getInvertedBinaryOpTypeFromTokenType(tokenType));

    assertEquals("Unexpected token type " + tokenType.toString(), thrown.getMessage());
    assertEquals(EXPECTED_NUMBER_OF_TOKEN_TYPES, TokenType.values().length);
  }

  @Test
  public void constructor_withIRTempAddress_setsProperties()
  {
    ConcreteImpl actual = new ConcreteImpl(
      OpType.ADD, // opType
      testIRConstAddress1, // arg1
      testIRConstAddress2, // arg2
      testIRTempAddress); // result

    assertEquals(OpType.ADD, actual.opType);
    assertSame(testIRConstAddress1, actual.arg1);
    assertSame(testIRConstAddress2, actual.arg2);
    assertSame(testIRTempAddress, actual.result);
  }

  @Test
  public void constructor_withIRLabelAddress_setsProperties()
  {
    ConcreteImpl actual = new ConcreteImpl(
      OpType.LT, // opType
      testIRConstAddress1, // arg1
      testIRConstAddress2, // arg2
      testIRLabelAddress); // result

    assertEquals(OpType.LT, actual.opType);
    assertSame(testIRConstAddress1, actual.arg1);
    assertSame(testIRConstAddress2, actual.arg2);
    assertSame(testIRLabelAddress, actual.result);
  }

  @Test
  public void constructor_withSingleAddress_setsProperties()
  {
    ConcreteImpl actual = new ConcreteImpl(
      OpType.LABEL, // opType
      testIRLabelAddress); // result

    assertEquals(OpType.LABEL, actual.opType);
    assertSame(testIRLabelAddress, actual.arg1);
    assertNull(actual.arg2);
    assertNull(actual.result);
  }

  @Test
  public void opType_normalScenario_returnsOpType()
  {
    OpType actual = new ConcreteImpl(
      OpType.ADD, // opType
      testIRConstAddress1, // arg1
      testIRConstAddress2, // arg2
      testIRTempAddress // result
    ).opType();

    assertEquals(OpType.ADD, actual);
  }

  @Test
  public void arg1_normalScenario_returnsArg1Address()
  {
    IRAddress actual = new ConcreteImpl(
      OpType.ADD, // opType
      testIRConstAddress1, // arg1
      testIRConstAddress2, // arg2
      testIRTempAddress // result
    ).arg1();

    assertSame(testIRConstAddress1, actual);
  }

  @Test
  public void arg2_normalScenario_returnsArg2Address()
  {
    IRAddress actual = new ConcreteImpl(
      OpType.ADD, // opType
      testIRConstAddress1, // arg1
      testIRConstAddress2, // arg2
      testIRTempAddress // result
    ).arg2();

    assertSame(testIRConstAddress2, actual);
  }

  @Test
  public void result_normalScenario_returnsResultAddress()
  {
    IRAddress actual = new ConcreteImpl(
      OpType.ADD, // opType
      testIRConstAddress1, // arg1
      testIRConstAddress2, // arg2
      testIRTempAddress // result
    ).result();

    assertSame(testIRTempAddress, actual);
  }

  @Test
  public void setResult_normalScenario_setsResultAddress()
  {
    IRTempAddress newResultAddress = new IRTempAddress("t2");

    ConcreteImpl actual = new ConcreteImpl(
      OpType.ADD, // opType
      testIRConstAddress1, // arg1
      testIRConstAddress2, // arg2
      testIRTempAddress); // result

    actual.setResult(newResultAddress);

    assertSame(newResultAddress, actual.result);
  }
}
