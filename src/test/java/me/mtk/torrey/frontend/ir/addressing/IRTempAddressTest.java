package me.mtk.torrey.frontend.ir.addressing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class IRTempAddressTest
{
  @Test
  public void constructor_normalScenario_setsProperties()
  {
    IRTempAddress actual = new IRTempAddress("t0");

    assertEquals(IRAddressingMode.TEMP, actual.mode());
    assertEquals("t0", actual.value());
  }

  @Test
  public void equals_instancesAreLogicallyEqual_returnsTrue()
  {
    IRTempAddress x = new IRTempAddress("t0");
    IRTempAddress y = new IRTempAddress("t0");

    assertTrue(x.equals(y));
  }

  @Test
  public void equals_instancesAreNotLogicallyEqual_returnsFalse()
  {
    IRTempAddress x = new IRTempAddress("t0");
    IRTempAddress y = new IRTempAddress("t1");

    assertFalse(x.equals(y));
  }
}
