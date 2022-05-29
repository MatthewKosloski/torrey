package me.mtk.torrey.frontend.ir.addressing;

import org.junit.Assert;
import org.junit.Test;

public class IRNameAddressTest
{
  @Test
  public void constructor_normalScenario_setsProperties()
  {
    IRNameAddress actual = new IRNameAddress("torrey_println");

    Assert.assertEquals(IRAddressingMode.NAME, actual.mode());
    Assert.assertEquals("torrey_println", actual.value());
  }

  @Test
  public void equals_instancesAreLogicallyEqual_returnsTrue()
  {
    IRNameAddress x = new IRNameAddress("torrey_println");
    IRNameAddress y = new IRNameAddress("torrey_println");

    Assert.assertTrue(x.equals(y));
  }

  @Test
  public void equals_instancesAreNotLogicallyEqual_returnsFalse()
  {
    IRNameAddress x = new IRNameAddress("torrey_println");
    IRNameAddress y = new IRNameAddress("torrey_print");

    Assert.assertFalse(x.equals(y));
  }
}
