package me.mtk.torrey.frontend.ir.addressing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class IRConstAddressTest
{
  @Test
  public void constructor_argumentIsLong_setsProperties()
  {
    IRConstAddress actual = new IRConstAddress(42);

    assertEquals(IRAddressingMode.CONSTANT, actual.mode());
    assertEquals((Long)42L, actual.value());
  }

  @Test
  public void constructor_argumentIsTrue_setsProperties()
  {
    IRConstAddress actual = new IRConstAddress(true);

    assertEquals(IRAddressingMode.CONSTANT, actual.mode());
    assertEquals((Long)1L, actual.value());
  }

  @Test
  public void constructor_argumentIsFalse_setsProperties()
  {
    IRConstAddress actual = new IRConstAddress(false);

    assertEquals(IRAddressingMode.CONSTANT, actual.mode());
    assertEquals((Long)0L, actual.value());
  }

  @Test
  public void from_argumentIsIRNullAddress_returnsEquivalentIRConstAddress()
  {
    IRConstAddress expected = new IRConstAddress(0);
    IRConstAddress actual = IRConstAddress.from(IRNullAddress.getInstance());

    assertEquals(expected, actual);
  }


  @Test
  public void equals_instancesAreLogicallyEqual_returnsTrue()
  {
    IRConstAddress x = new IRConstAddress(true);
    IRConstAddress y = new IRConstAddress(true);

    assertTrue(x.equals(y));
  }

  @Test
  public void equals_instancesAreNotLogicallyEqual_returnsFalse()
  {
    IRConstAddress x = new IRConstAddress(true);
    IRConstAddress y = new IRConstAddress(false);

    assertFalse(x.equals(y));
  }
}
