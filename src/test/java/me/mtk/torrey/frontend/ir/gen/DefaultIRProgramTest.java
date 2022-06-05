package me.mtk.torrey.frontend.ir.gen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import me.mtk.torrey.frontend.ir.addressing.IRAddress;
import me.mtk.torrey.frontend.ir.addressing.IRConstAddress;
import me.mtk.torrey.frontend.ir.addressing.IRLabelAddress;
import me.mtk.torrey.frontend.ir.addressing.IRTempAddress;
import me.mtk.torrey.frontend.ir.instructions.IRBinaryInst;
import me.mtk.torrey.frontend.ir.instructions.IRCopyInst;
import me.mtk.torrey.frontend.ir.instructions.IRGotoInst;
import me.mtk.torrey.frontend.ir.instructions.IRLabelInst;
import me.mtk.torrey.frontend.ir.instructions.Quadruple;
import me.mtk.torrey.frontend.lexer.TokenType;

public class DefaultIRProgramTest
{
  @Test
  public void constructor_normalScenario_setsProperties()
  {
    IRProgram program = new DefaultIRProgram();
    List<Quadruple> expectedList = new ArrayList<>();

    assertEquals(expectedList, program.quads());
  }

  @Test
  public void addQuad_normalScenario_addsQuadToList()
  {
    IRProgram program = new DefaultIRProgram();
    List<Quadruple> expectedList = new ArrayList<>();
    Quadruple quad = new IRBinaryInst(
      TokenType.PLUS,
      new IRConstAddress(1),
      new IRConstAddress(2),
      new IRTempAddress("t0"));
    expectedList.add(quad);

    program.addQuad(quad);

    assertEquals(expectedList, program.quads());
  }

  @Test
  public void addQuads_emptyList_quadListIsEmpty()
  {
    IRProgram program = new DefaultIRProgram();
    List<Quadruple> expectedList = new ArrayList<>();

    program.addQuads(new ArrayList<Quadruple>());

    assertEquals(expectedList, program.quads());
  }

  @Test
  public void addQuads_listOfQuads_quadListHasQuads()
  {
    IRProgram program = new DefaultIRProgram();
    Quadruple labelInst = new IRLabelInst(new IRLabelAddress("l0"));
    Quadruple copyInst = new IRCopyInst(
      new IRTempAddress("t0"),
      new IRConstAddress(1));

    List<Quadruple> expectedList = Arrays.asList(labelInst, copyInst);

    program.addQuads(Arrays.asList(labelInst, copyInst));

    assertEquals(expectedList, program.quads());
  }

  @Test
  public void equals_differentListOfQuads_returnsFalse()
  {
    IRProgram programA = new DefaultIRProgram();
    IRProgram programB = new DefaultIRProgram();
    Quadruple labelInst1 = new IRLabelInst(new IRLabelAddress("l1"));
    Quadruple gotoInst1 = new IRGotoInst(new IRLabelAddress("l1"));
    Quadruple labelInst2 = new IRLabelInst(new IRLabelAddress("l2"));
    Quadruple gotoInst2 = new IRGotoInst(new IRLabelAddress("l2"));

    programA.addQuads(Arrays.asList(labelInst1, gotoInst1));
    programB.addQuads(Arrays.asList(labelInst2, gotoInst2));

    assertFalse(programA.equals(programB));
  }

  @Test
  public void equals_reflexivity_returnsTrue()
  {
    IRProgram program = new DefaultIRProgram();
    Quadruple labelInst = new IRLabelInst(new IRLabelAddress("l1"));

    program.addQuad(labelInst);

    assertTrue(program.equals(program));
  }

  @Test
  public void equals_symmetry_returnsTrue()
  {
    IRProgram programA = new DefaultIRProgram();
    IRProgram programB = new DefaultIRProgram();
    Quadruple labelInst = new IRLabelInst(new IRLabelAddress("l1"));

    programA.addQuad(labelInst);
    programB.addQuad(labelInst);

    assertTrue(programA.equals(programB));
    assertTrue(programB.equals(programA));
  }

  @Test
  public void equals_transitivity_returnsTrue()
  {
    IRProgram programA = new DefaultIRProgram();
    IRProgram programB = new DefaultIRProgram();
    IRProgram programC = new DefaultIRProgram();
    Quadruple labelInst = new IRLabelInst(new IRLabelAddress("l1"));

    programA.addQuad(labelInst);
    programB.addQuad(labelInst);
    programC.addQuad(labelInst);

    assertTrue(programA.equals(programB));
    assertTrue(programB.equals(programC));
    assertTrue(programA.equals(programC));
  }

  @Test
  public void equals_consistency_returnsTrue()
  {
    IRProgram programA = new DefaultIRProgram();
    IRProgram programB = new DefaultIRProgram();
    Quadruple labelInst = new IRLabelInst(new IRLabelAddress("l1"));

    programA.addQuad(labelInst);
    programB.addQuad(labelInst);

    assertTrue(programA.equals(programB));
    assertTrue(programA.equals(programB));
  }

  @Test
  public void equals_argumentIsNull_returnsFalse()
  {
    IRProgram program = new DefaultIRProgram();
    Quadruple labelInst = new IRLabelInst(new IRLabelAddress("l1"));

    program.addQuad(labelInst);

    assertFalse(program.equals(null));
  }

  @Test
  public void hashCode_instancesAreEqual_hashCodesAreEqual()
  {
    IRProgram programA = new DefaultIRProgram();
    IRProgram programB = new DefaultIRProgram();
    Quadruple labelInst = new IRLabelInst(new IRLabelAddress("l1"));

    programA.addQuad(labelInst);
    programB.addQuad(labelInst);

    assertEquals(programA.hashCode(), programB.hashCode());
  }


  @Test
  public void toString_noQuads_returnsEmptyString()
  {
    IRProgram programA = new DefaultIRProgram();

    assertEquals("", programA.toString());
  }

  @Test
  public void toString_oneQuad_returnsStringRepresentation()
  {
    IRProgram programA = new DefaultIRProgram();
    Quadruple copyInst = new IRCopyInst(
      new IRTempAddress("t0"),
      new IRConstAddress(1));

    programA.addQuad(copyInst);

    assertEquals("t0 = 1", programA.toString());
  }

  @Test
  public void toString_multipleQuads_returnsStringRepresentation()
  {
    IRProgram programA = new DefaultIRProgram();

    IRAddress t0 = new IRTempAddress("t0");
    IRAddress t1 = new IRTempAddress("t1");
    IRAddress t2 = new IRTempAddress("t2");
    IRAddress _1 = new IRConstAddress(1);
    IRAddress _2 = new IRConstAddress(2);

    Quadruple copyInst1 = new IRCopyInst(t0, _1);
    Quadruple copyInst2 = new IRCopyInst(t1, _2);
    Quadruple binaryInst = new IRBinaryInst(TokenType.PLUS, t0, t1, t2);

    programA.addQuads(Arrays.asList(copyInst1, copyInst2, binaryInst));

    assertEquals("t0 = 1\nt1 = 2\nt2 = t0 + t1", programA.toString());
  }
}
