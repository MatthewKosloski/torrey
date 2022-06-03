package me.mtk.torrey.frontend.ir.instructions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import me.mtk.torrey.frontend.ir.addressing.IRAddress;
import me.mtk.torrey.frontend.ir.addressing.IRConstAddress;
import me.mtk.torrey.frontend.ir.addressing.IRLabelAddress;
import me.mtk.torrey.frontend.ir.instructions.Quadruple.OpType;

public class IRLabelInstTest
{
  private static final IRAddress IR_LABEL_ADDR = new IRLabelAddress("l0");
  private static final IRAddress IR_CONST_ADDR = new IRConstAddress(1);

  @Test
  public void constructor_normalScenario_setsProperties()
  {
    Quadruple actual = new IRLabelInst(IR_LABEL_ADDR);

    assertEquals(OpType.LABEL, actual.opType);
    assertSame(IR_LABEL_ADDR, actual.arg1);
    assertNull(actual.arg2);
    assertNull(actual.result);
  }

  @Test
  public void constructor_labelIsIncorrectType_throwsIllegalArgumentException()
  {
    assertThrows(
      IllegalArgumentException.class,
      () -> new IRLabelInst(IR_CONST_ADDR));
  }

  @Test
  public void constructor_nullArgument_throwsNullPointerException()
  {
    assertThrows(
      NullPointerException.class,
      () -> new IRLabelInst(null));
  }


  @Test
  public void toString_normalScenario_returnsStringRepresentation()
  {
    Quadruple actual = new IRLabelInst(IR_LABEL_ADDR);

    assertEquals("label l0:", actual.toString());
  }
}