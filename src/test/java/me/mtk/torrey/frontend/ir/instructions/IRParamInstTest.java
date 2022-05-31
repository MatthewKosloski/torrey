package me.mtk.torrey.frontend.ir.instructions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import me.mtk.torrey.frontend.ir.addressing.IRAddress;
import me.mtk.torrey.frontend.ir.addressing.IRTempAddress;
import me.mtk.torrey.frontend.ir.instructions.Quadruple.OpType;

public class IRParamInstTest
{
  private static final IRAddress IR_TEMP_ADDR = new IRTempAddress("t0");

  @Test
  public void constructor_normalScenario_setsProperties()
  {
    Quadruple actual = new IRParamInst(IR_TEMP_ADDR);

    assertEquals(OpType.PARAM, actual.opType);
    assertSame(IR_TEMP_ADDR, actual.arg1);
    assertNull(actual.arg2);
    assertNull(actual.result);
  }

  @Test
  public void constructor_nullArgument_throwsNullPointerException()
  {
    assertThrows(
      NullPointerException.class,
      () -> new IRParamInst(null));
  }


  @Test
  public void toString_normalScenario_returnsStringRepresentation()
  {
    Quadruple actual = new IRParamInst(IR_TEMP_ADDR);

    assertEquals("param t0", actual.toString());
  }
}
