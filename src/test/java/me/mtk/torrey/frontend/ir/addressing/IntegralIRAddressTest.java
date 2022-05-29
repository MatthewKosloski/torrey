package me.mtk.torrey.frontend.ir.addressing;

import org.junit.Assert;
import org.junit.Test;

public class IntegralIRAddressTest
{
  class ConcreteImpl extends IRIntegralAddress
  {
    public ConcreteImpl(long value)
    {
      super(value);
    }
  }

  @Test
  public void constructor_calledWithNumber_setsProperties()
  {
    ConcreteImpl impl = new ConcreteImpl(42);

    Assert.assertEquals(IRAddressingMode.CONSTANT, impl.mode());
    Assert.assertEquals((Long)42L, impl.value());
  }

  @Test
  public void mode_normalScenario_returnsConstantIRAddressingMode()
  {
    ConcreteImpl impl = new ConcreteImpl(42);

    Assert.assertEquals(IRAddressingMode.CONSTANT, impl.mode());
  }

  @Test
  public void value_normalScenario_returnsValueAsLong()
  {
    ConcreteImpl impl = new ConcreteImpl(42);

    Assert.assertEquals((Long)42L, impl.value());
  }

  @Test
  public void equals_differentValues_returnsFalse()
  {
    ConcreteImpl x = new ConcreteImpl(42);
    ConcreteImpl y = new ConcreteImpl(43);

    Assert.assertFalse(x.equals(y));
  }

  @Test
  public void equals_reflexivity_returnsTrue()
  {
    ConcreteImpl impl = new ConcreteImpl(42);

    Assert.assertTrue(impl.equals(impl));
  }

  @Test
  public void equals_symmetry_returnsTrue()
  {
    ConcreteImpl x = new ConcreteImpl(42);
    ConcreteImpl y = new ConcreteImpl(42);

    Assert.assertTrue(x.equals(y));
    Assert.assertTrue(y.equals(x));
  }

  @Test
  public void equals_transitivity_returnsTrue()
  {
    ConcreteImpl x = new ConcreteImpl(42);
    ConcreteImpl y = new ConcreteImpl(42);
    ConcreteImpl z = new ConcreteImpl(42);

    Assert.assertTrue(x.equals(y));
    Assert.assertTrue(y.equals(z));
    Assert.assertTrue(x.equals(z));
  }

  @Test
  public void equals_consistency_returnsTrue()
  {
    ConcreteImpl x = new ConcreteImpl(42);
    ConcreteImpl y = new ConcreteImpl(42);

    Assert.assertTrue(x.equals(y));
    Assert.assertTrue(x.equals(y));
  }

  @Test
  public void equals_argumentIsNull_returnsFalse()
  {
    ConcreteImpl x = new ConcreteImpl(42);

    Assert.assertFalse(x.equals(null));
  }

  @Test
  public void hashCode_instancesAreEqual_hashCodesAreEqual()
  {
    ConcreteImpl x = new ConcreteImpl(42);
    ConcreteImpl y = new ConcreteImpl(42);

    Assert.assertEquals(x.hashCode(), y.hashCode());
  }


  @Test
  public void toString_normalScenario_returnsStringRepresentation()
  {
    ConcreteImpl impl = new ConcreteImpl(42);

    Assert.assertEquals("42", impl.toString());
  }
}
