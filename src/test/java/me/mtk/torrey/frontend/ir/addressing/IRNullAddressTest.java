package me.mtk.torrey.frontend.ir.addressing;

import org.junit.Assert;
import org.junit.Test;

public class IRNullAddressTest
{
  @Test
  public void getInstance_normalScenario_returnsInstance()
  {
    IRNullAddress actual = IRNullAddress.getInstance();

    Assert.assertEquals(IRAddressingMode.CONSTANT, actual.mode());
    Assert.assertEquals((Long)0L, actual.value());
  }

  @Test
  public void getInstance_multipleInvocations_returnsSameInstance()
  {
    IRNullAddress x = IRNullAddress.getInstance();
    IRNullAddress y = IRNullAddress.getInstance();

    Assert.assertTrue(x == y);
  }
}
