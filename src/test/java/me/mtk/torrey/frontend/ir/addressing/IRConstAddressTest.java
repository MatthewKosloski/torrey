package me.mtk.torrey.frontend.ir.addressing;

import org.junit.Assert;
import org.junit.Test;

public class IRConstAddressTest
{
  @Test
  public void constructorThatTakesLong_normalScenario_setsProperties()
  {
    IRConstAddress actual = new IRConstAddress(42);

    Assert.assertEquals(IRAddressingMode.CONSTANT, actual.mode());
    Assert.assertEquals((Long)42L, actual.value());
  }

  @Test
  public void constructorThatTakesBoolean_argumentIsTrue_setsProperties()
  {
    IRConstAddress actual = new IRConstAddress(true);

    Assert.assertEquals(IRAddressingMode.CONSTANT, actual.mode());
    Assert.assertEquals((Long)1L, actual.value());
  }

  @Test
  public void constructorThatTakesBoolean_argumentIsFalse_setsProperties()
  {
    IRConstAddress actual = new IRConstAddress(false);

    Assert.assertEquals(IRAddressingMode.CONSTANT, actual.mode());
    Assert.assertEquals((Long)0L, actual.value());
  }

  @Test
  public void fromThatTakesIRNullAddress_normalScenario_returnsEquivalentIRConstAddress()
  {
    IRConstAddress expected = new IRConstAddress(0);
    IRConstAddress actual = IRConstAddress.from(IRNullAddress.getInstance());

    Assert.assertEquals(expected, actual);
  }


  @Test
  public void equals_instancesAreLogicallyEqual_returnsTrue()
  {
    IRConstAddress x = new IRConstAddress(true);
    IRConstAddress y = new IRConstAddress(true);

    Assert.assertTrue(x.equals(y));
  }

  @Test
  public void equals_instancesAreNotLogicallyEqual_returnsFalse()
  {
    IRConstAddress x = new IRConstAddress(true);
    IRConstAddress y = new IRConstAddress(false);

    Assert.assertFalse(x.equals(y));
  }
}
