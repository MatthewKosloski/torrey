package me.mtk.torrey.frontend.ir.addressing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class IRNameAddressTest
{
  @Test
  public void constructor_normalScenario_setsProperties()
  {
    IRNameAddress actual = new IRNameAddress("torrey_println");

    assertEquals(IRAddressingMode.NAME, actual.mode());
    assertEquals("torrey_println", actual.value());
  }

  @Test
  public void equals_instancesAreLogicallyEqual_returnsTrue()
  {
    IRNameAddress x = new IRNameAddress("torrey_println");
    IRNameAddress y = new IRNameAddress("torrey_println");

    assertTrue(x.equals(y));
  }

  @Test
  public void makeCopy_normalScenario_makesCopy()
  {
    IRNameAddress expected = new IRNameAddress("torrey_println");
    IRNameAddress actual = expected.makeCopy();

    assertEquals(expected, actual);
    assertNotSame(expected, actual);
  }

  @Test
  public void equals_instancesAreNotLogicallyEqual_returnsFalse()
  {
    IRNameAddress x = new IRNameAddress("torrey_println");
    IRNameAddress y = new IRNameAddress("torrey_print");

    assertFalse(x.equals(y));
  }
}
