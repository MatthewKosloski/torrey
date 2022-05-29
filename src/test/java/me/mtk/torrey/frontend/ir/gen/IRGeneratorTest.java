package me.mtk.torrey.frontend.ir.gen;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

import me.mtk.torrey.frontend.ast.IntegerExpr;
import me.mtk.torrey.frontend.ast.Program;

public class IRGeneratorTest
{
  @Test
  public void testConstructor()
  {
    Program p = new Program(Arrays.asList(new IntegerExpr(42)));
    IRGenerator gen = new IRGenerator(p);

    // assertEquals(gen., actual);
  }
}
