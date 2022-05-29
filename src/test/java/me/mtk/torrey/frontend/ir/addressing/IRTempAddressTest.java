package me.mtk.torrey.frontend.ir.addressing;

import org.junit.Assert;
import org.junit.Test;

public class IRTempAddressTest
{
  @Test
  public void constructor_normalScenario_setsProperties()
  {
    IRTempAddress actual = new IRTempAddress("t0");

    Assert.assertEquals(IRAddressingMode.TEMP, actual.mode());
    Assert.assertEquals("t0", actual.value());
  }

  @Test
  public void equals_instancesAreLogicallyEqual_returnsTrue()
  {
    IRTempAddress x = new IRTempAddress("t0");
    IRTempAddress y = new IRTempAddress("t0");

    Assert.assertTrue(x.equals(y));
  }

  @Test
  public void equals_instancesAreNotLogicallyEqual_returnsFalse()
  {
    IRTempAddress x = new IRTempAddress("t0");
    IRTempAddress y = new IRTempAddress("t1");

    Assert.assertFalse(x.equals(y));
  }
}
