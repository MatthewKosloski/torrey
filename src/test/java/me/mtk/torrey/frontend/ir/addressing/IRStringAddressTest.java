package me.mtk.torrey.frontend.ir.addressing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class IRStringAddressTest
{
  class ConcreteImpl extends IRStringAddress
  {
    public ConcreteImpl(IRAddressingMode mode, String value)
    {
      super(mode, value);
    }

    @Override
    public IRStringAddress makeCopy()
    {
      return new ConcreteImpl(this.mode(), this.value());
    }
  }

  @Test
  public void constructor_calledWithAModeAndValue_setsProperties()
  {
    ConcreteImpl impl = new ConcreteImpl(IRAddressingMode.TEMP, "t42");

    assertEquals(IRAddressingMode.TEMP, impl.mode());
    assertEquals("t42", impl.value());
  }

  @Test
  public void mode_normalScenario_returnsTheIRAddressingMode()
  {
    ConcreteImpl impl = new ConcreteImpl(IRAddressingMode.LABEL, "l0");

    assertEquals(IRAddressingMode.LABEL, impl.mode());
  }

  @Test
  public void value_normalScenario_returnsValueAsString()
  {
    ConcreteImpl impl = new ConcreteImpl(IRAddressingMode.NAME, "torrey_println");

    assertEquals("torrey_println", impl.value());
  }

  @Test
  public void equals_differentModes_returnsFalse()
  {
    ConcreteImpl x = new ConcreteImpl(IRAddressingMode.TEMP, "l0");
    ConcreteImpl y = new ConcreteImpl(IRAddressingMode.LABEL, "l0");

    assertFalse(x.equals(y));
  }

  @Test
  public void equals_differentValues_returnsFalse()
  {
    ConcreteImpl x = new ConcreteImpl(IRAddressingMode.LABEL, "l0");
    ConcreteImpl y = new ConcreteImpl(IRAddressingMode.LABEL, "l1");

    assertFalse(x.equals(y));
  }

  @Test
  public void equals_reflexivity_returnsTrue()
  {
    ConcreteImpl impl = new ConcreteImpl(IRAddressingMode.LABEL, "l0");

    assertTrue(impl.equals(impl));
  }

  @Test
  public void equals_symmetry_returnsTrue()
  {
    ConcreteImpl x = new ConcreteImpl(IRAddressingMode.LABEL, "l0");
    ConcreteImpl y = new ConcreteImpl(IRAddressingMode.LABEL, "l0");

    assertTrue(x.equals(y));
    assertTrue(y.equals(x));
  }

  @Test
  public void equals_transitivity_returnsTrue()
  {
    ConcreteImpl x = new ConcreteImpl(IRAddressingMode.LABEL, "l0");
    ConcreteImpl y = new ConcreteImpl(IRAddressingMode.LABEL, "l0");
    ConcreteImpl z = new ConcreteImpl(IRAddressingMode.LABEL, "l0");

    assertTrue(x.equals(y));
    assertTrue(y.equals(z));
    assertTrue(x.equals(z));
  }

  @Test
  public void equals_consistency_returnsTrue()
  {
    ConcreteImpl x = new ConcreteImpl(IRAddressingMode.LABEL, "l0");
    ConcreteImpl y = new ConcreteImpl(IRAddressingMode.LABEL, "l0");

    assertTrue(x.equals(y));
    assertTrue(x.equals(y));
  }

  @Test
  public void equals_argumentIsNull_returnsFalse()
  {
    ConcreteImpl x = new ConcreteImpl(IRAddressingMode.LABEL, "l0");

    assertFalse(x.equals(null));
  }

  @Test
  public void hashCode_instancesAreEqual_hashCodesAreEqual()
  {
    ConcreteImpl x = new ConcreteImpl(IRAddressingMode.LABEL, "l0");
    ConcreteImpl y = new ConcreteImpl(IRAddressingMode.LABEL, "l0");


    assertEquals(x.hashCode(), y.hashCode());
  }

  @Test
  public void toString_normalScenario_returnsStringRepresentation()
  {
    ConcreteImpl impl = new ConcreteImpl(IRAddressingMode.TEMP, "t42");

    assertEquals("t42", impl.toString());
  }
}
