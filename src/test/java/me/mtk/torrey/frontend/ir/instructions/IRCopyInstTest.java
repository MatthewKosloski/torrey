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
import me.mtk.torrey.frontend.ir.addressing.IRTempAddress;
import me.mtk.torrey.frontend.ir.instructions.Quadruple.OpType;

public class IRCopyInstTest
{
  private static final IRAddress IR_CONST_ADDR = new IRConstAddress(1);
  private static final IRAddress IR_TEMP_ADDR = new IRTempAddress("t0");

  public static Stream<Arguments> nullArgumentCombos()
  {
    return Stream.of(
      arguments(null, IR_CONST_ADDR),
      arguments(IR_TEMP_ADDR, null),
      arguments(null, null));
  }

  @Test
  public void constructor_normalScenario_setsProperties()
  {
    Quadruple actual = new IRCopyInst(
      IR_TEMP_ADDR,
      IR_CONST_ADDR);

    assertEquals(OpType.COPY, actual.opType);
    assertSame(IR_CONST_ADDR, actual.arg1);
    assertNull(actual.arg2);
    assertSame(IR_TEMP_ADDR, actual.result);
  }

  @Test
  public void constructor_lhsIsIncorrectType_throwsIllegalArgumentException()
  {
    assertThrows(
      IllegalArgumentException.class,
      () -> new IRCallInst(
        IR_CONST_ADDR,
        IR_CONST_ADDR));
  }

  @ParameterizedTest
  @MethodSource("nullArgumentCombos")
  public void constructor_nullArguments_throwsNullPointerException(
    IRAddress lhs,
    IRAddress rhs
  )
  {
    assertThrows(
      NullPointerException.class,
      () -> new IRCopyInst(lhs, rhs));
  }


  @Test
  public void toString_normalScenario_returnsStringRepresentation()
  {
    Quadruple actual = new IRCopyInst(
      IR_TEMP_ADDR,
      IR_CONST_ADDR);

    assertEquals("t0 = 1", actual.toString());
  }
}
