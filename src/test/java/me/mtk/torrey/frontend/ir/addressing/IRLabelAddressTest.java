package me.mtk.torrey.frontend.ir.addressing;

import org.junit.Assert;
import org.junit.Test;

public class IRLabelAddressTest
{
  @Test
  public void constructor_normalScenario_setsProperties()
  {
    IRLabelAddress actual = new IRLabelAddress("l0");

    Assert.assertEquals(IRAddressingMode.LABEL, actual.mode());
    Assert.assertEquals("l0", actual.value());
  }

  @Test
  public void equals_instancesAreLogicallyEqual_returnsTrue()
  {
    IRLabelAddress x = new IRLabelAddress("l0");
    IRLabelAddress y = new IRLabelAddress("l0");

    Assert.assertTrue(x.equals(y));
  }

  @Test
  public void equals_instancesAreNotLogicallyEqual_returnsFalse()
  {
    IRLabelAddress x = new IRLabelAddress("l0");
    IRLabelAddress y = new IRLabelAddress("l1");

    Assert.assertFalse(x.equals(y));
  }
}
