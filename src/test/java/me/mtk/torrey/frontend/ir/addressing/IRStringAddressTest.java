package me.mtk.torrey.frontend.ir.addressing;

import org.junit.Assert;
import org.junit.Test;

public class IRStringAddressTest
{
  class ConcreteImpl extends IRStringAddress
  {
    public ConcreteImpl(IRAddressingMode mode, String value)
    {
      super(mode, value);
    }
  }

  @Test
  public void constructor_calledWithAModeAndValue_setsProperties()
  {
    ConcreteImpl impl = new ConcreteImpl(IRAddressingMode.TEMP, "t42");

    Assert.assertEquals(IRAddressingMode.TEMP, impl.mode());
    Assert.assertEquals("t42", impl.value());
  }

  @Test
  public void mode_normalScenario_returnsTheIRAddressingMode()
  {
    ConcreteImpl impl = new ConcreteImpl(IRAddressingMode.LABEL, "l0");

    Assert.assertEquals(IRAddressingMode.LABEL, impl.mode());
  }

  @Test
  public void value_normalScenario_returnsValueAsString()
  {
    ConcreteImpl impl = new ConcreteImpl(IRAddressingMode.NAME, "torrey_println");

    Assert.assertEquals("torrey_println", impl.value());
  }

  @Test
  public void equals_differentModes_returnsFalse()
  {
    ConcreteImpl x = new ConcreteImpl(IRAddressingMode.TEMP, "l0");
    ConcreteImpl y = new ConcreteImpl(IRAddressingMode.LABEL, "l0");

    Assert.assertFalse(x.equals(y));
  }

  @Test
  public void equals_differentValues_returnsFalse()
  {
    ConcreteImpl x = new ConcreteImpl(IRAddressingMode.LABEL, "l0");
    ConcreteImpl y = new ConcreteImpl(IRAddressingMode.LABEL, "l1");

    Assert.assertFalse(x.equals(y));
  }

  @Test
  public void equals_reflexivity_returnsTrue()
  {
    ConcreteImpl impl = new ConcreteImpl(IRAddressingMode.LABEL, "l0");

    Assert.assertTrue(impl.equals(impl));
  }

  @Test
  public void equals_symmetry_returnsTrue()
  {
    ConcreteImpl x = new ConcreteImpl(IRAddressingMode.LABEL, "l0");
    ConcreteImpl y = new ConcreteImpl(IRAddressingMode.LABEL, "l0");

    Assert.assertTrue(x.equals(y));
    Assert.assertTrue(y.equals(x));
  }

  @Test
  public void equals_transitivity_returnsTrue()
  {
    ConcreteImpl x = new ConcreteImpl(IRAddressingMode.LABEL, "l0");
    ConcreteImpl y = new ConcreteImpl(IRAddressingMode.LABEL, "l0");
    ConcreteImpl z = new ConcreteImpl(IRAddressingMode.LABEL, "l0");

    Assert.assertTrue(x.equals(y));
    Assert.assertTrue(y.equals(z));
    Assert.assertTrue(x.equals(z));
  }

  @Test
  public void equals_consistency_returnsTrue()
  {
    ConcreteImpl x = new ConcreteImpl(IRAddressingMode.LABEL, "l0");
    ConcreteImpl y = new ConcreteImpl(IRAddressingMode.LABEL, "l0");

    Assert.assertTrue(x.equals(y));
    Assert.assertTrue(x.equals(y));
  }

  @Test
  public void equals_argumentIsNull_returnsFalse()
  {
    ConcreteImpl x = new ConcreteImpl(IRAddressingMode.LABEL, "l0");

    Assert.assertFalse(x.equals(null));
  }

  @Test
  public void hashCode_instancesAreEqual_hashCodesAreEqual()
  {
    ConcreteImpl x = new ConcreteImpl(IRAddressingMode.LABEL, "l0");
    ConcreteImpl y = new ConcreteImpl(IRAddressingMode.LABEL, "l0");


    Assert.assertEquals(x.hashCode(), y.hashCode());
  }

  @Test
  public void toString_normalScenario_returnsStringRepresentation()
  {
    ConcreteImpl impl = new ConcreteImpl(IRAddressingMode.TEMP, "t42");

    Assert.assertEquals("t42", impl.toString());
  }
}
