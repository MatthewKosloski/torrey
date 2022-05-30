package me.mtk.torrey.frontend.ir.instructions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;
import me.mtk.torrey.frontend.ir.addressing.IRConstAddress;
import me.mtk.torrey.frontend.ir.addressing.IRNameAddress;
import me.mtk.torrey.frontend.ir.instructions.Quadruple.OpType;

public class IRCallInstTest
{
  private static final IRConstAddress IR_CONST_ADDR = new IRConstAddress(1);
  private static final IRNameAddress IR_NAME_ADDR = new IRNameAddress("println");

  @Test
  public void constructor_normalScenario_setsProperties()
  {
    IRCallInst actual = new IRCallInst(
      IR_NAME_ADDR,
      IR_CONST_ADDR);

    assertEquals(OpType.CALL, actual.opType);
    assertSame(IR_NAME_ADDR, actual.arg1);
    assertSame(IR_CONST_ADDR, actual.arg2);
    assertNull(actual.result);
  }

  @Test
  public void toString_normalScenario_returnsStringRepresentation()
  {
    IRCallInst actual = new IRCallInst(
      IR_NAME_ADDR,
      IR_CONST_ADDR);

    assertEquals("call println, 1", actual.toString());
  }
}
