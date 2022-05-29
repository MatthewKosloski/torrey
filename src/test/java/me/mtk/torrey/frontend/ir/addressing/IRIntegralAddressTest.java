package me.mtk.torrey.frontend.ir.addressing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class IRIntegralAddressTest
{
  class ConcreteImpl extends IRIntegralAddress
  {
    public ConcreteImpl(long value)
    {
      super(value);
    }

    public ConcreteImpl(IRIntegralAddress address)
    {
      super(address);
    }
  }

  @Test
  public void constructor_calledWithNumber_setsProperties()
  {
    ConcreteImpl impl = new ConcreteImpl(42);

    assertEquals(IRAddressingMode.CONSTANT, impl.mode());
    assertEquals((Long)42L, impl.value());
  }

  @Test
  public void constructor_withIRStringAddress_createsCopy()
  {
    ConcreteImpl original = new ConcreteImpl(42);
    ConcreteImpl copy = new ConcreteImpl(original);

    assertEquals(copy, original);
    assertNotSame(copy, original);
  }

  @Test
  public void mode_normalScenario_returnsConstantIRAddressingMode()
  {
    ConcreteImpl impl = new ConcreteImpl(42);

    assertEquals(IRAddressingMode.CONSTANT, impl.mode());
  }

  @Test
  public void value_normalScenario_returnsValueAsLong()
  {
    ConcreteImpl impl = new ConcreteImpl(42);

    assertEquals((Long)42L, impl.value());
  }

  @Test
  public void equals_differentValues_returnsFalse()
  {
    ConcreteImpl x = new ConcreteImpl(42);
    ConcreteImpl y = new ConcreteImpl(43);

    assertFalse(x.equals(y));
  }

  @Test
  public void equals_reflexivity_returnsTrue()
  {
    ConcreteImpl impl = new ConcreteImpl(42);

    assertTrue(impl.equals(impl));
  }

  @Test
  public void equals_symmetry_returnsTrue()
  {
    ConcreteImpl x = new ConcreteImpl(42);
    ConcreteImpl y = new ConcreteImpl(42);

    assertTrue(x.equals(y));
    assertTrue(y.equals(x));
  }

  @Test
  public void equals_transitivity_returnsTrue()
  {
    ConcreteImpl x = new ConcreteImpl(42);
    ConcreteImpl y = new ConcreteImpl(42);
    ConcreteImpl z = new ConcreteImpl(42);

    assertTrue(x.equals(y));
    assertTrue(y.equals(z));
    assertTrue(x.equals(z));
  }

  @Test
  public void equals_consistency_returnsTrue()
  {
    ConcreteImpl x = new ConcreteImpl(42);
    ConcreteImpl y = new ConcreteImpl(42);

    assertTrue(x.equals(y));
    assertTrue(x.equals(y));
  }

  @Test
  public void equals_argumentIsNull_returnsFalse()
  {
    ConcreteImpl x = new ConcreteImpl(42);

    assertFalse(x.equals(null));
  }

  @Test
  public void hashCode_instancesAreEqual_hashCodesAreEqual()
  {
    ConcreteImpl x = new ConcreteImpl(42);
    ConcreteImpl y = new ConcreteImpl(42);

    assertEquals(x.hashCode(), y.hashCode());
  }


  @Test
  public void toString_normalScenario_returnsStringRepresentation()
  {
    ConcreteImpl impl = new ConcreteImpl(42);

    assertEquals("42", impl.toString());
  }
}
