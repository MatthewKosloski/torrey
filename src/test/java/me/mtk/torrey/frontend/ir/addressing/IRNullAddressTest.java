package me.mtk.torrey.frontend.ir.addressing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class IRNullAddressTest
{
  @Test
  public void getInstance_normalScenario_returnsInstance()
  {
    IRNullAddress actual = IRNullAddress.getInstance();

    assertEquals(IRAddressingMode.CONSTANT, actual.mode());
    assertEquals((Long)0L, actual.value());
  }

  @Test
  public void getInstance_multipleInvocations_returnsSameInstance()
  {
    IRNullAddress x = IRNullAddress.getInstance();
    IRNullAddress y = IRNullAddress.getInstance();

    assertTrue(x == y);
  }
}
