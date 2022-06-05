package me.mtk.torrey.frontend.ir.gen;

import java.util.*;
import me.mtk.torrey.frontend.ast.*;
import me.mtk.torrey.frontend.ast.Expr.DataType;
import me.mtk.torrey.frontend.ir.addressing.*;
import me.mtk.torrey.frontend.ir.instructions.*;
import me.mtk.torrey.frontend.lexer.TokenType;
import me.mtk.torrey.frontend.symbols.Env;

/**
 * Converts the given abstract syntax tree to a more low-level,
 * linear intermediate representation, or IR. This IR is a type
 * of three-address code represented as a collection of quadruples
 * of the form (operator, argument1, argument2, result).
 */
public final class DefaultIRGenerator implements IRGenerator
{
    // The IR Program being generated.
    private IRProgram irProgram;

    // The AST from which IR instructions will
    // be generated.
    private Program program;

    // The current environment.
    private Env top;

    // The most recently calculated address.
    private IRAddress nextAddress;

    private int nextIRLabelNumber = 0;

    private int nextIRTempNumber = 0;

    /**
     * Instantiates a new IRGenVisitor with an abstract
     * syntax tree to be converted to an IR program.
     *
     * @param program The AST from which IR instructions
     * will be generated.
     */
    public DefaultIRGenerator(Program program)
    {
      this.irProgram = new DefaultIRProgram();
      this.program = program;
    }

    /**
     * Generate the IR instructions, returning the
     * resulting IR program.
     *
     * @return A intermediate program composed of a
     * linear sequence of quadruples.
     */
    public IRProgram gen()
    {
      program.accept(this);
      return irProgram;
    }

    /**
     * Generates IR instructions for every expression
     * in the given program.
     *
     * @param Program The root AST node.
     * @return null.
     */
    public void visit(Program program)
    {
      // For every AST node, call the appropriate
      // visit() method to generate the IR instruction
      // corresponding to that node.
      for (ASTNode child : program.children())
      {
        child.accept(this);
      }
    }

    public void visit(IntegerExpr expr)
    {
      if (expr.parent() instanceof UnaryExpr
        || expr.parent() instanceof BinaryExpr
        || expr.parent() instanceof LetBinding)
      {
        final IRAddress address = new IRConstAddress(expr.toConstant());
        nextAddress = address;
      }
      else
      {
        final IRAddress result = newIRTempAddress();
        final IRAddress rhs = new IRConstAddress(expr.toConstant());

        irProgram.addQuad(new IRCopyInst(result, rhs));

        nextAddress = result;
      }
    }

    public void visit(BooleanExpr expr)
    {
      final IRTempAddress result = newIRTempAddress();
      final boolean bool = expr.token().type() == TokenType.TRUE;
      final IRConstAddress rhs = new IRConstAddress(bool);

      irProgram.addQuad(new IRCopyInst(result, rhs));

      nextAddress = result;
    }

    /**
     * Generates one or more IR instructions for the
     * given unary AST node.
     *
     * @param expr An unary AST node.
     * @return The destination address of the
     * result of the given AST node.
     */
    public void visit(UnaryExpr expr)
    {
      final TokenType tokType = expr.token().type();
      final IRTempAddress result = newIRTempAddress();

      (expr.first()).accept(this);
      final IRAddress arg = nextAddress;

      if (expr.token().type() == TokenType.MINUS && expr.first() instanceof IntegerExpr)
      {
        irProgram.addQuad(new IRCopyInst(result, arg));
      }
      else
      {
        irProgram.addQuad(new IRUnaryInst(tokType, arg, result));
      }

      nextAddress = result;
    }

    /**
     * Generates one or more IR instructions for the
     * given arithmetic AST node.
     *
     * @param expr An arithmetic AST node.
     * @return The destination address of the
     * result of the arithmetic instruction.
     */
    public void visit(ArithmeticExpr expr)
    {
      final IRTempAddress result = newIRTempAddress();
      final TokenType tokType = expr.token().type();

      (expr.first()).accept(this);
      final IRAddress arg1 = nextAddress;

      (expr.second()).accept(this);
      final IRAddress arg2 = nextAddress;

      irProgram.addQuad(new IRBinaryInst(tokType, arg1, arg2, result));

      nextAddress = result;
    }

    /**
     * Generates one or more IR instructions for the
     * given binary comparison AST node.
     *
     * @param expr A binary comparison expression.
     * @return The label of the false branch.
     */
    public void visit(CompareExpr expr)
    {
      final TokenType tokType = expr.token().type();
      final IRLabelAddress label = newIRLabel();
      final IRTempAddress comparisonResult = newIRTempAddress();

      // Recursively generate IR instructions for the operands
      (expr.first()).accept(this);
      final IRAddress arg1 = nextAddress;

      (expr.second()).accept(this);
      final IRAddress arg2 = nextAddress;

      irProgram.addQuad(new IRCopyInst(comparisonResult, new IRConstAddress(0)));
      irProgram.addQuad(new IRIfInst(tokType, arg1, arg2, label));
      irProgram.addQuad(new IRCopyInst(comparisonResult, new IRConstAddress(1)));
      irProgram.addQuad(new IRLabelInst(label));

      nextAddress = comparisonResult;
    }

    /**
     * Generates one or more IR instructions for the
     * given print expression.
     *
     * @param expr A print expression.
     * @return A null address, indicating that this expression
     * does not evaluate to a value.
     */
    public void visit(PrintExpr expr)
    {
      // Accumulate the param instructions to be
      // inserted directly before the call instruction.
      final List<Quadruple> params = new ArrayList<>();

      // Generate the instructions for the parameters.
      for (ASTNode child : expr.children())
      {
        child.accept(this);
        params.add(new IRParamInst(nextAddress));
      }

      final IRNameAddress procName = new IRNameAddress(
        expr.token().rawText());
      final IRConstAddress numParams = new IRConstAddress(
        expr.children().size());

      irProgram.addQuads(params);
      irProgram.addQuad(new IRCallInst(procName, numParams));

      nextAddress = IRNullAddress.getInstance();
    }

    /**
     * Generates one or more IR instructions for the
     * given let expression.
     *
     * @param expr A let expression.
     * @return If the let expression has one or more
     * expressions in its body, then the destination address
     * of the last expression of the body is returned; otherwise,
     * a null address is returned, indicating that the let expression
     * does not evaluate to a value.
     */
    public void visit(LetExpr expr)
    {
      // Cache the previous environment and activate
      // the environment of this expression.
      final Env prevEnv = top;
      top = expr.environment();

      // Generate IR instructions for the expressions bounded
      // to the identifiers and populate the symbol table.
      ((LetBindings) expr.first()).accept(this);

      IRAddress returnAddress = null;

      // Recursively generate IR instructions for the body expressions
      for (int i = 1; i < expr.children().size(); i++)
      {
        final Expr child = (Expr) expr.children().get(i);
        child.accept(this);

        // Return the destination address of the last
        // expression of the body.
        if (i == expr.children().size() - 1)
        {
          returnAddress = nextAddress;
        }
      }

      // Restore the previous environment.
      top = prevEnv;

      if (returnAddress == null)
      {
        returnAddress = IRNullAddress.getInstance();
      }

      nextAddress = returnAddress;
    }

    /**
     * Generates IR instructions for each LetBinding
     * AST node within the given LetBindings node.
     *
     * @param bindings A LetBindings AST node.
     * @return null.
     */
    public void visit(LetBindings bindings)
    {
      for (ASTNode binding : bindings.children())
      {
        ((LetBinding) binding).accept(this);
      }
    }

     /**
     * Generates IR instructions for the bounded
     * expression and stores its source address in
     * the Symbol pointed to by the identifier
     * to which the expression is bound.
     *
     * @param bindings A LetBinding AST node.
     * @return null.
     */
    public void visit(LetBinding binding)
    {
      final IRTempAddress result = newIRTempAddress();
      final String id = binding.first().token().rawText();

      // The source of the copy instruction is the
      // destination of the bounded expression.
      (binding.second()).accept(this);
      final IRAddress rhs = nextAddress;

      // Store the source address of the bounded expression
      // in the symbol table.
      top.get(id).setAddress(result);

      if (!(rhs instanceof IRNullAddress))
      {
        irProgram.addQuad(new IRCopyInst(result, rhs));
      }
    }

    /**
     * Traverses the lexical-scope chain, looking
     * for the destination address of the expression
     * bound to the given identifier.
     *
     * @param expr An identifier expression.
     * @return The destination address of the expression
     * bound to the given identifier.
     */
    public void visit(IdentifierExpr expr)
    {
      nextAddress = top.get(expr.token().rawText()).address();
    }

    public void visit(IfExpr expr)
    {
      // Recursively generate IR instructions for the test condition
      expr.test().accept(this);
      IRAddress testResultAddr = nextAddress;

      if (testResultAddr instanceof IRNullAddress)
      {
        // The test condition does not evaluate to a value, so
        // we'll convert its NullAddress to a constant
        testResultAddr = IRConstAddress.from((IRNullAddress) testResultAddr);
      }

      // Jump to the else label if the test condition is false
      final IRLabelAddress elseLabel = newIRLabel();

      if (expr.test().evalType() == DataType.INTEGER)
      {
        // The test evaluates to an integer, so compare it with 0
        // and jump to the else label if the test evaluates to 0
        irProgram.addQuad(new IRIfInst(
          TokenType.NOT,
          testResultAddr,
          new IRConstAddress(0),
          elseLabel));
      }
      else
      {
        irProgram.addQuad(new IRIfInst(
          TokenType.EQUAL,
          testResultAddr,
          new IRConstAddress(1),
          elseLabel));
      }

      final IRTempAddress branchResultAddr = newIRTempAddress();

      // Recursively generate IR instructions for the then branch
      expr.consequent().accept(this);
      final IRAddress consequentResultAddr = nextAddress;

      // Indicates whether the expression evaluates to a value
      boolean hasResult = false;

      if (consequentResultAddr instanceof IRTempAddress)
      {
        // Update lhs of the then branch
        irProgram.quads().get(irProgram.quads().size() - 1).setResult(branchResultAddr);
        hasResult = true;
      }

      // After the then branch, we should jump to the done label
      // to skip over the else block
      final IRLabelAddress doneLabel = newIRLabel();
      irProgram.addQuad(new IRGotoInst(doneLabel));

      // Start of else block
      irProgram.addQuad(new IRLabelInst(elseLabel));

      if (expr instanceof IfThenElseExpr)
      {
        // Recursively generate an explicit else branch
        ((IfThenElseExpr) expr).alternative().accept(this);
        final IRAddress alternativeResultAddr = nextAddress;

        if (alternativeResultAddr instanceof IRTempAddress)
        {
          // Update lhs of the else branch
          irProgram.quads().get(irProgram.quads().size() - 1).setResult(branchResultAddr);
          hasResult = true;
        }
      }
      else
      {
        // Generate an implicit else branch
        irProgram.addQuad(new IRCopyInst(branchResultAddr, new IRConstAddress(0)));
      }

      // Generate the done label
      irProgram.addQuad(new IRLabelInst(doneLabel));

      if (hasResult)
      {
        nextAddress = branchResultAddr;
      } else
      {
        nextAddress = IRNullAddress.getInstance();
      }
    }

    public void visit(IfThenElseExpr expr)
    {
      visit((IfExpr)expr);
    }

    private IRLabelAddress newIRLabel()
    {
      return new IRLabelAddress(String.format("l%d", nextIRLabelNumber++));
    }

    private IRTempAddress newIRTempAddress()
    {
      return new IRTempAddress(String.format("t%d", nextIRTempNumber++));
    }
}
