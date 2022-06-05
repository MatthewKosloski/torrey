package me.mtk.torrey.frontend.ir.gen;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import me.mtk.torrey.frontend.ast.*;
import me.mtk.torrey.frontend.ir.addressing.*;
import me.mtk.torrey.frontend.ir.instructions.*;
import me.mtk.torrey.frontend.lexer.Token;
import me.mtk.torrey.frontend.lexer.TokenType;
import me.mtk.torrey.frontend.symbols.*;

public class DefaultIRGeneratorTest
{
  @Test
  public void gen_withoutAST_noIRInstrsAreGenerated()
  {
    IRGenerator generator = newGenerator();

    IRProgram actual = generator.gen();

    assertEquals(new DefaultIRProgram(), actual);
  }

  @Test
  public void gen_withAST_generatesIRInstrs()
  {
    // 42
    IRGenerator generator = newGenerator(
      new IntegerExpr(42));

    // t0 = 42
    IRProgram expected = buildIRProgram(
      new IRCopyInst(
        new IRTempAddress("t0"),
        new IRConstAddress(42)));

    IRProgram actual = generator.gen();

    assertEquals(expected, actual);
  }

  @Test
  public void visitIntegerExpr_normalScenario_generatesIRInstrs()
  {
    IRGenerator generator = newGenerator();

    IRProgram expected = buildIRProgram(
      // t0 = 42
      new IRCopyInst(
        new IRTempAddress("t0"),
        new IRConstAddress(42)),
      // param t0
      new IRParamInst(new IRTempAddress("t0")),
      // call print, 1
      new IRCallInst(
        new IRNameAddress("print"),
        new IRConstAddress(1)));

    // (print 42)
    IRProgram actual = generator.gen();
    generator.visit(
      new PrintExpr(
        new Token(TokenType.PRINT, "print"),
        Arrays.asList(new IntegerExpr(42))));

    assertEquals(expected, actual);
  }

  @Test
  public void visitIntegerExpr_parentIsCompareExpr_generatesIRInstrs()
  {
    IRGenerator generator = newGenerator();

    // t0 = 0
    // if 1 != 0 goto l0
    // t0 = 1
    // label l0:
    IRProgram expected = buildIRProgram(
      // t0 = 0
      new IRCopyInst(
          new IRTempAddress("t0"),
          new IRConstAddress(0)),
      // if 1 != 0 goto l0
      new IRIfInst(
        TokenType.EQUAL,
        new IRConstAddress(1),
        new IRConstAddress(0),
        new IRLabelAddress("l0")),
      // t0 = 1
      new IRCopyInst(
        new IRTempAddress("t0"),
        new IRConstAddress(1)),
      // label l0:
      new IRLabelInst(new IRLabelAddress("l0")));

    // (== 1 0)
    IRProgram actual = generator.gen();
    generator.visit(
      new CompareExpr(
        new Token(TokenType.EQUAL, "=="),
        new IntegerExpr(1),
        new IntegerExpr(0)));

    assertEquals(expected, actual);
  }

  @Test
  public void visitIntegerExpr_parentIsLetBinding_generatesIRInstrs()
  {
    IRGenerator generator = newGenerator();

    // t0 = 42
    IRProgram expected = buildIRProgram(
      new IRCopyInst(
        new IRTempAddress("t0"),
        new IRConstAddress(42)));

    Env env = new Env(null);
    env.put("x", new Symbol("x", SymCategory.VARIABLE, null));

    LetExpr letExpr = new LetExpr(
      new Token(TokenType.LET, "let"),
      new LetBindings(Arrays.asList(
        new LetBinding(
          new IdentifierExpr(new Token(TokenType.IDENTIFIER, "x")),
          new IntegerExpr(42)))),
      new ArrayList<Expr>());

    letExpr.setEnv(env);

    // (let [x 42])
    IRProgram actual = generator.gen();
    generator.visit(letExpr);

    assertEquals(expected, actual);
  }

  @Test
  public void visitIntegerExpr_parentIsUnaryExpr_generatesIRInstrs()
  {
    IRGenerator generator = newGenerator();

    // t0 = -42
    IRProgram expected = buildIRProgram(
      new IRCopyInst(
        new IRTempAddress("t0"),
        new IRConstAddress(-42)));

    // (- 42)
    IRProgram actual = generator.gen();
    generator.visit(
      new UnaryExpr(
        new Token(TokenType.MINUS, "-"),
        new IntegerExpr(42)));

    assertEquals(expected, actual);
  }

  @Test
  public void visitIntegerExpr_parentIsArithmeticExpr_generatesIRInstrs()
  {
    IRGenerator generator = newGenerator();

    // t0 = 32 + 10
    IRProgram expected = buildIRProgram(
      new IRBinaryInst(
        TokenType.PLUS,
        new IRConstAddress(32),
        new IRConstAddress(10),
        new IRTempAddress("t0")));

    // (+ 32 10)
    IRProgram actual = generator.gen();
    generator.visit(
      new ArithmeticExpr(
        new Token(TokenType.PLUS, "+"),
        new IntegerExpr(32),
        new IntegerExpr(10)));

    assertEquals(expected, actual);
  }

  @Test
  public void visitBooleanExpr_true_generatesIRInstrs()
  {
    IRGenerator generator = newGenerator();

    // t0 = 1
    IRProgram expected = buildIRProgram(
      new IRCopyInst(
        new IRTempAddress("t0"),
        new IRConstAddress(1)));

    // true
    IRProgram actual = generator.gen();
    generator.visit(
      new BooleanExpr(new Token(TokenType.TRUE, "true")));

    assertEquals(expected, actual);
  }

  @Test
  public void visitBooleanExpr_false_generatesIRInstrs()
  {
    IRGenerator generator = newGenerator();

    // t0 = 0
    IRProgram expected = buildIRProgram(
      new IRCopyInst(
        new IRTempAddress("t0"),
        new IRConstAddress(0)));

    // false
    IRProgram actual = generator.gen();
    generator.visit(
      new BooleanExpr(new Token(TokenType.FALSE, "false")));

    assertEquals(expected, actual);
  }

  @Test
  public void visitBooleanExpr_hasParentExpr_generatesIRInstrs()
  {
    IRGenerator generator = newGenerator();

    // t1 = 1 (true)
    // t2 = 0 (false)
    // t0 = 0
    // if t1 != t2 goto l0
    // t0 = 1
    // label l0:
    IRProgram expected = buildIRProgram(
      // t1 = 1
      new IRCopyInst(
          new IRTempAddress("t1"),
          new IRConstAddress(1)),
      // t2 = 0
      new IRCopyInst(
          new IRTempAddress("t2"),
          new IRConstAddress(0)),
      // t0 = 0
      new IRCopyInst(
          new IRTempAddress("t0"),
          new IRConstAddress(0)),
      // if t1 != t2 goto l0
      new IRIfInst(
        TokenType.EQUAL,
        new IRTempAddress("t1"),
        new IRTempAddress("t2"),
        new IRLabelAddress("l0")),
      // t0 = 1
      new IRCopyInst(
        new IRTempAddress("t0"),
        new IRConstAddress(1)),
      // label l0:
      new IRLabelInst(new IRLabelAddress("l0")));

    // (== true false)
    IRProgram actual = generator.gen();
    generator.visit(
      new CompareExpr(
        new Token(TokenType.EQUAL, "=="),
        new BooleanExpr(new Token(TokenType.TRUE, "true")),
        new BooleanExpr(new Token(TokenType.FALSE, "false"))));

    assertEquals(expected, actual);
  }

  @Test
  public void visitUnaryExpr_hasParentExpr_generatesIRInstrs()
  {
    IRGenerator generator = newGenerator();

    // t1 = 1 + 2
    // t0 = - t1
    // param t0
    // call print, 1
    IRProgram expected = buildIRProgram(
      // t1 = 1 + 2
      new IRBinaryInst(
        TokenType.PLUS,
        new IRConstAddress(1),
        new IRConstAddress(2),
        new IRTempAddress("t1")),
      // t0 = - t1
      new IRUnaryInst(
        TokenType.MINUS,
        new IRTempAddress("t1"),
        new IRTempAddress("t0")),
      // param t0
      new IRParamInst(new IRTempAddress("t0")),
      // call print, 1
      new IRCallInst(
        new IRNameAddress("print"),
        new IRConstAddress(1)));

    // (print (- (+ 1 2)))
    IRProgram actual = generator.gen();
    generator.visit(
      new PrintExpr(
        new Token(TokenType.PRINT, "print"),
        Arrays.asList(
          new UnaryExpr(
            new Token(TokenType.MINUS, "-"),
            new ArithmeticExpr(
              new Token(TokenType.PLUS, "+"),
              new IntegerExpr(1),
              new IntegerExpr(2))))));

    assertEquals(expected, actual);
  }

  @Test
  public void visitUnaryExpr_unaryMinusExprAndChildIsAnInteger_generatesIRCopyInst()
  {
    IRGenerator generator = newGenerator();

    // t0 = -42
    IRProgram expected = buildIRProgram(
      new IRCopyInst(
        new IRTempAddress("t0"),
        new IRConstAddress(-42)));

    // (- 42)
    IRProgram actual = generator.gen();
    generator.visit(
      new UnaryExpr(
        new Token(TokenType.MINUS, "-"),
        new IntegerExpr(42)));

    assertEquals(expected, actual);
  }

  @Test
  public void visitArithmeticExpr_hasParentExpr_generatesIRInstrs()
  {
    IRGenerator generator = newGenerator();

    // t0 = 1 + 2
    // param t0
    // call print, 1
    IRProgram expected = buildIRProgram(
      // t0 = 1 + 2
      new IRBinaryInst(
        TokenType.PLUS,
        new IRConstAddress(1),
        new IRConstAddress(2),
        new IRTempAddress("t0")),
      // param t0
      new IRParamInst(new IRTempAddress("t0")),
      // call print, 1
      new IRCallInst(
        new IRNameAddress("print"),
        new IRConstAddress(1)));

    // (print (+ 1 2))
    IRProgram actual = generator.gen();
    generator.visit(
      new PrintExpr(
        new Token(TokenType.PRINT, "print"),
        Arrays.asList(
          new ArithmeticExpr(
            new Token(TokenType.PLUS, "-"),
            new IntegerExpr(1),
            new IntegerExpr(2)))));

    assertEquals(expected, actual);
  }

  @Test
  public void visitCompareExpr_hasParentExpr_generatesIRInstrs()
  {
    IRGenerator generator = newGenerator();

    // t0 = 0
    // if 1 != 2 goto l0
    // t0 = 1
    // label l0:
    // if t0 != 1 goto l1
    // t1 = 3
    // goto l2
    // label l1:
    // t1 = 0
    // label l2:
    IRProgram expected = buildIRProgram(
      // t0 = 0
      new IRCopyInst(
        new IRTempAddress("t0"),
        new IRConstAddress(0)),
      // if 1 != 2 goto l0
      new IRIfInst(
        TokenType.EQUAL,
        new IRConstAddress(1),
        new IRConstAddress(2),
        new IRLabelAddress("l0")),
      // t0 = 1
      new IRCopyInst(
        new IRTempAddress("t0"),
        new IRConstAddress(1)),
      // label l0:
      new IRLabelInst(new IRLabelAddress("l0")),
      // if t0 != 1 goto l1
      new IRIfInst(
        TokenType.EQUAL,
        new IRTempAddress("t0"),
        new IRConstAddress(1),
        new IRLabelAddress("l1")),
      // t1 = 3
      new IRCopyInst(
        new IRTempAddress("t1"),
        new IRConstAddress(3)),
      // goto l2
      new IRGotoInst(new IRLabelAddress("l2")),
      // label l1:
      new IRLabelInst(new IRLabelAddress("l1")),
      // t1 = 0
      new IRCopyInst(
        new IRTempAddress("t1"),
        new IRConstAddress(0)),
      // label l2:
      new IRLabelInst(new IRLabelAddress("l2")));

    // (if (== 1 2) 3)
    IRProgram actual = generator.gen();
    generator.visit(
      new IfExpr(
        new Token(TokenType.IF, "if"),
        new CompareExpr(
          new Token(TokenType.EQUAL, "=="),
          new IntegerExpr(1),
          new IntegerExpr(2)),
          new IntegerExpr(3))
    );

    assertEquals(expected, actual);
  }

  @ParameterizedTest
  @EnumSource(names = { "PRINT", "PRINTLN" })
  public void visitPrintExpr_oneChild_generatesIRInstrs(TokenType tokType)
  {
    IRGenerator generator = newGenerator();

    // t0 = 42
    // param t0
    // call print, 1 AND call println, 1
    IRProgram expected = buildIRProgram(
      // t0 = 42
      new IRCopyInst(
        new IRTempAddress("t0"),
        new IRConstAddress(42)),
      // param t0
      new IRParamInst(new IRTempAddress("t0")),
      // call print, 1 AND call println, 1
      new IRCallInst(
        new IRNameAddress(tokType.toString().toLowerCase()),
        new IRConstAddress(1)));

    // (print 42) AND (println 42)
    IRProgram actual = generator.gen();
    generator.visit(
      new PrintExpr(
        new Token(tokType, tokType.toString().toLowerCase()),
        Arrays.asList(
          new IntegerExpr(42))));

    assertEquals(expected, actual);
  }

  @ParameterizedTest
  @EnumSource(names = { "PRINT", "PRINTLN" })
  public void visitPrintExpr_moreThanOneChild_generatesIRInstrs(TokenType tokType)
  {
    IRGenerator generator = newGenerator();

    // t0 = 42
    // t1 = 43
    // param t0
    // param t1
    // call print, 2 AND call println, 2
    IRProgram expected = buildIRProgram(
      // t0 = 42
      new IRCopyInst(
        new IRTempAddress("t0"),
        new IRConstAddress(42)),
      // t0 = 43
      new IRCopyInst(
        new IRTempAddress("t1"),
        new IRConstAddress(43)),
      // param t0
      new IRParamInst(new IRTempAddress("t0")),
      // param t1
      new IRParamInst(new IRTempAddress("t1")),
      // call print, 2 AND call println, 2
      new IRCallInst(
        new IRNameAddress(tokType.toString().toLowerCase()),
        new IRConstAddress(2)));

    // (print 42 43) AND (println 42 43)
    IRProgram actual = generator.gen();
    generator.visit(
      new PrintExpr(
        new Token(tokType, tokType.toString().toLowerCase()),
        Arrays.asList(
          new IntegerExpr(42),
          new IntegerExpr(43))));

    assertEquals(expected, actual);
  }

  @ParameterizedTest
  @EnumSource(names = { "PRINT", "PRINTLN" })
  public void visitPrintExpr_hasParentExpr_generatesIRInstrs(TokenType tokType)
  {
    IRGenerator generator = newGenerator();

    // t0 = 42
    // param t0
    // call print, 1 AND call println, 1
    IRProgram expected = buildIRProgram(
      // t0 = 42
      new IRCopyInst(
        new IRTempAddress("t0"),
        new IRConstAddress(42)),
      // param t0
      new IRParamInst(new IRTempAddress("t0")),
      // call print, 1 AND call println, 1
      new IRCallInst(
        new IRNameAddress(tokType.toString().toLowerCase()),
        new IRConstAddress(1)));

    //(let [] (print 42)) AND (let [] (println 42))
    IRProgram actual = generator.gen();
    generator.visit(
      new LetExpr(
        new Token(TokenType.LET, "let"),
        new LetBindings(new ArrayList<LetBinding>()),
        Arrays.asList(
          new PrintExpr(
            new Token(tokType, tokType.toString().toLowerCase()),
            Arrays.asList(
              new IntegerExpr(42))))));

    assertEquals(expected, actual);
  }

  private IRProgram buildIRProgram(Quadruple... quads)
  {
    IRProgram program = new DefaultIRProgram();
    program.addQuads(Arrays.asList(quads));

    return program;
  }

  private IRGenerator newGenerator()
  {
    return new DefaultIRGenerator(
      new Program(new ArrayList<Expr>()));
  }

  private IRGenerator newGenerator(Expr... exprs)
  {
    return new DefaultIRGenerator(
      new Program(Arrays.asList(exprs)));
  }
}
