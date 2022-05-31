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
import org.junit.jupiter.params.provider.MethodSource;
import me.mtk.torrey.frontend.ir.addressing.IRAddress;
import me.mtk.torrey.frontend.ir.addressing.IRConstAddress;
import me.mtk.torrey.frontend.ir.addressing.IRNameAddress;
import me.mtk.torrey.frontend.ir.instructions.Quadruple.OpType;

public class IRCallInstTest
{
  private static final IRAddress IR_CONST_ADDR = new IRConstAddress(1);
  private static final IRAddress IR_NAME_ADDR = new IRNameAddress("println");

  public static Stream<Arguments> nullArgumentCombos()
  {
    return Stream.of(
      arguments(null, IR_CONST_ADDR),
      arguments(IR_NAME_ADDR, null),
      arguments(null, null));
  }

  @Test
  public void constructor_normalScenario_setsProperties()
  {
    Quadruple actual = new IRCallInst(
      IR_NAME_ADDR,
      IR_CONST_ADDR);

    assertEquals(OpType.CALL, actual.opType);
    assertSame(IR_NAME_ADDR, actual.arg1);
    assertSame(IR_CONST_ADDR, actual.arg2);
    assertNull(actual.result);
  }

  @Test
  public void constructor_procNameIsIncorrectType_throwsIllegalArgumentException()
  {
    assertThrows(
      IllegalArgumentException.class,
      () -> new IRCallInst(
        IR_CONST_ADDR,
        IR_CONST_ADDR));
  }

  @Test
  public void constructor_numParamsIsIncorrectType_throwsIllegalArgumentException()
  {
    assertThrows(
      IllegalArgumentException.class,
      () -> new IRCallInst(
        IR_CONST_ADDR,
        IR_NAME_ADDR));
  }

  @ParameterizedTest
  @MethodSource("nullArgumentCombos")
  public void constructor_nullArguments_throwsNullPointerException(
    IRAddress procName, IRAddress numParams)
  {
    assertThrows(
      NullPointerException.class,
      () -> new IRCallInst(procName, numParams));
  }

  @Test
  public void toString_normalScenario_returnsStringRepresentation()
  {
    Quadruple actual = new IRCallInst(
      IR_NAME_ADDR,
      IR_CONST_ADDR);

    assertEquals("call println, 1", actual.toString());
  }
}
