package me.mtk.torrey.frontend.ir.addressing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class IRLabelAddressTest
{
  @Test
  public void constructor_normalScenario_setsProperties()
  {
    IRLabelAddress actual = new IRLabelAddress("l0");

    assertEquals(IRAddressingMode.LABEL, actual.mode());
    assertEquals("l0", actual.value());
  }

  @Test
  public void equals_instancesAreLogicallyEqual_returnsTrue()
  {
    IRLabelAddress x = new IRLabelAddress("l0");
    IRLabelAddress y = new IRLabelAddress("l0");

    assertTrue(x.equals(y));
  }

  @Test
  public void equals_instancesAreNotLogicallyEqual_returnsFalse()
  {
    IRLabelAddress x = new IRLabelAddress("l0");
    IRLabelAddress y = new IRLabelAddress("l1");

    assertFalse(x.equals(y));
  }
}
