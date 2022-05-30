package me.mtk.torrey.frontend.ir.instructions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;
import me.mtk.torrey.frontend.ir.addressing.IRConstAddress;
import me.mtk.torrey.frontend.ir.addressing.IRTempAddress;
import me.mtk.torrey.frontend.ir.instructions.Quadruple.OpType;

public class IRCopyInstTest
{
  private static final IRConstAddress IR_CONST_ADDR = new IRConstAddress(1);
  private static final IRTempAddress IR_TEMP_ADDR = new IRTempAddress("t0");

  @Test
  public void constructor_normalScenario_setsProperties()
  {
    IRCopyInst actual = new IRCopyInst(
      IR_TEMP_ADDR,
      IR_CONST_ADDR);

    assertEquals(OpType.COPY, actual.opType);
    assertSame(IR_CONST_ADDR, actual.arg1);
    assertNull(actual.arg2);
    assertSame(IR_TEMP_ADDR, actual.result);
  }

  @Test
  public void toString_normalScenario_returnsStringRepresentation()
  {
    IRCopyInst actual = new IRCopyInst(
      IR_TEMP_ADDR,
      IR_CONST_ADDR);

    assertEquals("t0 = 1", actual.toString());
  }
}
