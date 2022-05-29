package me.mtk.torrey.frontend.ir.instructions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
  private static final IRConstAddress IR_CONST_ADDR_1 = new IRConstAddress(10);
  private static final IRConstAddress IR_CONST_ADDR_2 = new IRConstAddress(32);
  private static final IRLabelAddress IR_LABEL_ADDR = new IRLabelAddress("l0");
  private static final IRTempAddress IR_TEMP_ADDR = new IRTempAddress("t0");

  private class ConcreteImpl extends Quadruple
  {
    public ConcreteImpl(OpType opType, IRAddress arg1, IRAddress arg2, IRTempAddress result)
    {
      super(opType, arg1, arg2, result);
    }

    public ConcreteImpl(OpType opType, IRAddress arg1, IRAddress arg2, IRLabelAddress result)
    {
      super(opType, arg1, arg2, result);
    }

    public ConcreteImpl(OpType opType, IRAddress arg1, IRAddress arg2)
    {
      super(opType, arg1, arg2);
    }

    public ConcreteImpl(OpType opType, IRAddress arg)
    {
      super(opType, arg);
    }

    public ConcreteImpl(ConcreteImpl impl)
    {
      super(impl);
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
  public void constructor_withIRTempAddressAndNonNullArgs_setsProperties()
  {
    ConcreteImpl actual = new ConcreteImpl(
      OpType.ADD, // opType
      IR_CONST_ADDR_1, // arg1
      IR_CONST_ADDR_2, // arg2
      IR_TEMP_ADDR); // result

    assertEquals(OpType.ADD, actual.opType);
    assertSame(IR_CONST_ADDR_1, actual.arg1);
    assertSame(IR_CONST_ADDR_2, actual.arg2);
    assertSame(IR_TEMP_ADDR, actual.result);
  }

  public static Stream<Arguments> provider6()
  {
    return Stream.of(
      arguments(null, IR_CONST_ADDR_1, IR_CONST_ADDR_2, IR_TEMP_ADDR),
      arguments(OpType.ADD, null, IR_CONST_ADDR_2, IR_TEMP_ADDR),
      arguments(OpType.ADD, IR_CONST_ADDR_1, null, IR_TEMP_ADDR),
      arguments(OpType.ADD, IR_CONST_ADDR_1, IR_CONST_ADDR_2, null));
  }

  @ParameterizedTest
  @MethodSource("provider6")
  public void constructor_withIRTempAddressAndNullArgs_throwsNullPointerException(
    OpType opType,
    IRAddress arg1,
    IRAddress arg2,
    IRTempAddress result
  )
  {
    NullPointerException thrown = assertThrows(
      NullPointerException.class,
      () -> new ConcreteImpl(opType, arg1, arg2, (IRTempAddress) result));

    assertNotNull(thrown);
  }

  @Test
  public void constructor_withIRLabelAddressAndNonNullArgs_setsProperties()
  {
    ConcreteImpl actual = new ConcreteImpl(
      OpType.LT, // opType
      IR_CONST_ADDR_1, // arg1
      IR_CONST_ADDR_2, // arg2
      IR_LABEL_ADDR); // result

    assertEquals(OpType.LT, actual.opType);
    assertSame(IR_CONST_ADDR_1, actual.arg1);
    assertSame(IR_CONST_ADDR_2, actual.arg2);
    assertSame(IR_LABEL_ADDR, actual.result);
  }

  public static Stream<Arguments> provider7()
  {
    return Stream.of(
      arguments(null, IR_CONST_ADDR_1, IR_CONST_ADDR_2, IR_LABEL_ADDR),
      arguments(OpType.LT, null, IR_CONST_ADDR_2, IR_LABEL_ADDR),
      arguments(OpType.LT, IR_CONST_ADDR_1, null, IR_LABEL_ADDR),
      arguments(OpType.LT, IR_CONST_ADDR_1, IR_CONST_ADDR_2, null));
  }

  @ParameterizedTest
  @MethodSource("provider7")
  public void constructor_withIRLabelAddressAndNullArgs_throwsNullPointerException(
    OpType opType,
    IRAddress arg1,
    IRAddress arg2,
    IRLabelAddress result
  )
  {
    NullPointerException thrown = assertThrows(
      NullPointerException.class,
      () -> new ConcreteImpl(opType, arg1, arg2, (IRLabelAddress) result));

    assertNotNull(thrown);
  }

  @Test
  public void constructor_withSingleAddressAndNonNullArgs_setsProperties()
  {
    ConcreteImpl actual = new ConcreteImpl(
      OpType.LABEL, // opType
      IR_LABEL_ADDR); // result

    assertEquals(OpType.LABEL, actual.opType);
    assertSame(IR_LABEL_ADDR, actual.arg1);
    assertNull(actual.arg2);
    assertNull(actual.result);
  }

  public static Stream<Arguments> provider8()
  {
    return Stream.of(
      arguments(null, IR_LABEL_ADDR),
      arguments(OpType.LABEL, null));
  }

  @ParameterizedTest
  @MethodSource("provider8")
  public void constructor_withSingleAddressAndNullArgs_throwsNullPointerException(OpType opType, IRAddress arg)
  {
    NullPointerException thrown = assertThrows(
      NullPointerException.class,
      () -> new ConcreteImpl(opType, arg));

    assertNotNull(thrown);
  }

  @Test
  public void constructor_withOpTypeAndArgsAndNonNullArgs_setsProperties()
  {
    ConcreteImpl actual = new ConcreteImpl(
      OpType.ADD, // opType
      IR_CONST_ADDR_1, // arg1
      IR_CONST_ADDR_2); // arg2

    assertEquals(OpType.ADD, actual.opType);
    assertSame(IR_CONST_ADDR_1, actual.arg1);
    assertSame(IR_CONST_ADDR_2, actual.arg2);
    assertNull(actual.result);
  }

  public static Stream<Arguments> provider10()
  {
    return Stream.of(
      arguments(null, IR_CONST_ADDR_1, IR_CONST_ADDR_2),
      arguments(OpType.LABEL, null, IR_CONST_ADDR_2),
      arguments(OpType.LABEL, IR_CONST_ADDR_1, null));
  }

  @ParameterizedTest
  @MethodSource("provider10")
  public void constructor_withOpTypeAndArgsAndNullArgs_throwsNullPointerException(
    OpType opType,
    IRAddress arg1,
    IRAddress arg2)
  {
    NullPointerException thrown = assertThrows(
      NullPointerException.class,
      () -> new ConcreteImpl(opType, arg1, arg2));

    assertNotNull(thrown);
  }

  @Test
  public void constructor_withQuadruple_makesCopy()
  {
    ConcreteImpl original = new ConcreteImpl(
      OpType.ADD, // opType
      IR_CONST_ADDR_1, // arg1
      IR_CONST_ADDR_2, // arg2
      IR_TEMP_ADDR); // result

    ConcreteImpl copy = new ConcreteImpl(original);

    assertEquals(original.opType, copy.opType);
    assertEquals(original.arg1, copy.arg1);
    assertNotSame(original.arg1, copy.arg1);
    assertEquals(original.arg2, copy.arg2);
    assertNotSame(original.arg2, copy.arg2);
    assertEquals(original.result, copy.result);
    assertNotSame(original.result, copy.result);
  }

  @Test
  public void constructor_withTuple_makesCopy()
  {
    ConcreteImpl original = new ConcreteImpl(
      OpType.LABEL, // opType
      IR_LABEL_ADDR); // arg1

    ConcreteImpl copy = new ConcreteImpl(original);

    assertEquals(original.opType, copy.opType);
    assertEquals(original.arg1, copy.arg1);
    assertNotSame(original.arg1, copy.arg1);
    assertNull(copy.arg2);
    assertNull(copy.result);
  }

  @Test
  public void opType_normalScenario_returnsOpType()
  {
    OpType actual = new ConcreteImpl(
      OpType.ADD, // opType
      IR_CONST_ADDR_1, // arg1
      IR_CONST_ADDR_2, // arg2
      IR_TEMP_ADDR // result
    ).opType();

    assertEquals(OpType.ADD, actual);
  }

  @Test
  public void arg1_normalScenario_returnsArg1Address()
  {
    IRAddress actual = new ConcreteImpl(
      OpType.ADD, // opType
      IR_CONST_ADDR_1, // arg1
      IR_CONST_ADDR_2, // arg2
      IR_TEMP_ADDR // result
    ).arg1();

    assertSame(IR_CONST_ADDR_1, actual);
  }

  @Test
  public void arg2_normalScenario_returnsArg2Address()
  {
    IRAddress actual = new ConcreteImpl(
      OpType.ADD, // opType
      IR_CONST_ADDR_1, // arg1
      IR_CONST_ADDR_2, // arg2
      IR_TEMP_ADDR // result
    ).arg2();

    assertSame(IR_CONST_ADDR_2, actual);
  }

  @Test
  public void result_normalScenario_returnsResultAddress()
  {
    IRAddress actual = new ConcreteImpl(
      OpType.ADD, // opType
      IR_CONST_ADDR_1, // arg1
      IR_CONST_ADDR_2, // arg2
      IR_TEMP_ADDR // result
    ).result();

    assertSame(IR_TEMP_ADDR, actual);
  }

  @Test
  public void setResult_normalScenario_setsResultAddress()
  {
    IRTempAddress newResultAddress = new IRTempAddress("t2");

    ConcreteImpl actual = new ConcreteImpl(
      OpType.ADD, // opType
      IR_CONST_ADDR_1, // arg1
      IR_CONST_ADDR_2, // arg2
      IR_TEMP_ADDR); // result

    actual.setResult(newResultAddress);

    assertSame(newResultAddress, actual.result);
  }

  @Test
  public void equals_differentOpTypes_returnsFalse()
  {
    ConcreteImpl x = new ConcreteImpl(
      OpType.ADD, // opType
      IR_CONST_ADDR_1, // arg1
      IR_CONST_ADDR_2, // arg2
      IR_TEMP_ADDR); // result

    ConcreteImpl y = new ConcreteImpl(
      OpType.SUB, // opType
      IR_CONST_ADDR_1, // arg1
      IR_CONST_ADDR_2, // arg2
      IR_TEMP_ADDR); // arg1

    assertFalse(x.equals(y));
  }

  @Test
  public void equals_differentArg1_returnsFalse()
  {
    ConcreteImpl x = new ConcreteImpl(
      OpType.ADD, // opType
      new IRConstAddress(10), // arg1
      IR_CONST_ADDR_2, // arg2
      IR_TEMP_ADDR); // result

    ConcreteImpl y = new ConcreteImpl(
      OpType.SUB, // opType
      new IRConstAddress(32), // arg1
      IR_CONST_ADDR_2, // arg2
      IR_TEMP_ADDR); // arg1

    assertFalse(x.equals(y));
  }

  @Test
  public void equals_differentArg2_returnsFalse()
  {
    ConcreteImpl x = new ConcreteImpl(
      OpType.ADD, // opType
      IR_CONST_ADDR_1, // arg1
      new IRConstAddress(10), // arg2
      IR_TEMP_ADDR); // result

    ConcreteImpl y = new ConcreteImpl(
      OpType.SUB, // opType
      IR_CONST_ADDR_1, // arg1
      new IRConstAddress(32), // arg2
      IR_TEMP_ADDR); // arg1

    assertFalse(x.equals(y));
  }

  @Test
  public void equals_differentResult_returnsFalse()
  {
    ConcreteImpl x = new ConcreteImpl(
      OpType.ADD, // opType
      IR_CONST_ADDR_1, // arg1
      IR_CONST_ADDR_2, // arg2
      new IRTempAddress("t0")); // result

    ConcreteImpl y = new ConcreteImpl(
      OpType.SUB, // opType
      IR_CONST_ADDR_1, // arg1
      IR_CONST_ADDR_2, // arg2
      new IRTempAddress("t1")); // arg1

    assertFalse(x.equals(y));
  }

  @Test
  public void equals_lhsIsTupleAndRhsIsQuadruple_returnsFalse()
  {
    ConcreteImpl x = new ConcreteImpl(
      OpType.LABEL, // opType
      IR_LABEL_ADDR); // arg1

    ConcreteImpl y = new ConcreteImpl(
      OpType.SUB, // opType
      IR_CONST_ADDR_1, // arg1
      IR_CONST_ADDR_2, // arg2
      IR_TEMP_ADDR); // arg1

      assertFalse(x.equals(y));
  }

  @Test
  public void equals_lhsIsQuadrupleAndRhsIsTuple_returnsFalse()
  {
    ConcreteImpl x = new ConcreteImpl(
      OpType.LABEL, // opType
      IR_LABEL_ADDR); // arg1

    ConcreteImpl y = new ConcreteImpl(
      OpType.SUB, // opType
      IR_CONST_ADDR_1, // arg1
      IR_CONST_ADDR_2, // arg2
      IR_TEMP_ADDR); // arg1

      assertFalse(y.equals(x));
  }

  public static Stream<Arguments> provider9()
  {
    return Stream.of(
      arguments(true, false, false),
      arguments(false, true, false),
      arguments(true, true, true));
  }

  @ParameterizedTest
  @MethodSource("provider9")
  public void equals_withTuples_returnsEquality(boolean isLhsATuple, boolean isRhsATuple, boolean expectedEquality)
  {
    ConcreteImpl tuple = new ConcreteImpl(
      OpType.ADD, // opType
      IR_CONST_ADDR_1); // arg1

    ConcreteImpl quadruple = new ConcreteImpl(
      OpType.ADD, // opType
      IR_CONST_ADDR_1, // arg1
      IR_CONST_ADDR_2, // arg2
      IR_TEMP_ADDR); // result

    ConcreteImpl lhs = isLhsATuple ? tuple : quadruple;
    ConcreteImpl rhs = isRhsATuple ? tuple : quadruple;

    assertEquals(expectedEquality, lhs.equals(rhs));
  }


  @Test
  public void equals_reflexivity_returnsTrue()
  {
    ConcreteImpl x = new ConcreteImpl(
      OpType.ADD, // opType
      IR_CONST_ADDR_1, // arg1
      IR_CONST_ADDR_2, // arg2
      IR_TEMP_ADDR); // result

    assertTrue(x.equals(x));
  }

  @Test
  public void equals_symmetry_returnsTrue()
  {
    ConcreteImpl x = new ConcreteImpl(
      OpType.ADD, // opType
      IR_CONST_ADDR_1, // arg1
      IR_CONST_ADDR_2, // arg2
      IR_TEMP_ADDR); // result

    ConcreteImpl y = new ConcreteImpl(
      OpType.ADD, // opType
      IR_CONST_ADDR_1, // arg1
      IR_CONST_ADDR_2, // arg2
      IR_TEMP_ADDR); // result

    assertTrue(x.equals(y));
    assertTrue(y.equals(x));
  }

  @Test
  public void equals_transitivity_returnsTrue()
  {
    ConcreteImpl x = new ConcreteImpl(
      OpType.ADD, // opType
      IR_CONST_ADDR_1, // arg1
      IR_CONST_ADDR_2, // arg2
      IR_TEMP_ADDR); // result

    ConcreteImpl y = new ConcreteImpl(
      OpType.ADD, // opType
      IR_CONST_ADDR_1, // arg1
      IR_CONST_ADDR_2, // arg2
      IR_TEMP_ADDR); // result

    ConcreteImpl z = new ConcreteImpl(
      OpType.ADD, // opType
      IR_CONST_ADDR_1, // arg1
      IR_CONST_ADDR_2, // arg2
      IR_TEMP_ADDR); // result

    assertTrue(x.equals(y));
    assertTrue(y.equals(z));
    assertTrue(x.equals(z));
  }

  @Test
  public void equals_consistency_returnsTrue()
  {
    ConcreteImpl x = new ConcreteImpl(
      OpType.ADD, // opType
      IR_CONST_ADDR_1, // arg1
      IR_CONST_ADDR_2, // arg2
      IR_TEMP_ADDR); // result

    ConcreteImpl y = new ConcreteImpl(
      OpType.ADD, // opType
      IR_CONST_ADDR_1, // arg1
      IR_CONST_ADDR_2, // arg2
      IR_TEMP_ADDR); // result

    assertTrue(x.equals(y));
    assertTrue(x.equals(y));
  }

  @Test
  public void equals_argumentIsNull_returnsFalse()
  {
    ConcreteImpl x = new ConcreteImpl(
      OpType.ADD, // opType
      IR_CONST_ADDR_1, // arg1
      IR_CONST_ADDR_2, // arg2
      IR_TEMP_ADDR); // result

    assertFalse(x.equals(null));
  }

  @Test
  public void hashCode_instancesAreEqual_hashCodesAreEqual()
  {
    ConcreteImpl x = new ConcreteImpl(
      OpType.ADD, // opType
      IR_CONST_ADDR_1, // arg1
      IR_CONST_ADDR_2, // arg2
      IR_TEMP_ADDR); // result

    ConcreteImpl y = new ConcreteImpl(
      OpType.ADD, // opType
      IR_CONST_ADDR_1, // arg1
      IR_CONST_ADDR_2, // arg2
      IR_TEMP_ADDR); // result


    assertEquals(x.hashCode(), y.hashCode());
  }
}
