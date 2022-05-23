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
public final class IRGenerator implements ASTNodeVisitor<IRAddress>
{
    // The IR Program being generated.
    private IRProgram irProgram;

    // The AST from which IR instructions will
    // be generated.
    private Program program;

    // The current environment.
    private Env top;

    /**
     * Instantiates a new IRGenVisitor with an abstract
     * syntax tree to be converted to an IR program.
     *
     * @param program The AST from which IR instructions
     * will be generated.
     */
    public IRGenerator(Program program)
    {
      this.irProgram = new IRProgram();
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
    public IRAddress visit(Program program)
    {
      // For every AST node, call the appropriate
      // visit() method to generate the IR instruction
      // corresponding to that node.
      for (ASTNode child : program.children())
        child.accept(this);

      return null;
    }

    public IRAddress visit(IntegerExpr expr)
    {
      final IRTempAddress result = new IRTempAddress();
      final IRConstAddress rhs = new IRConstAddress(expr.toConstant());
      irProgram.addQuad(new IRCopyInst(result, rhs));
      return result;
    }

    public IRAddress visit(BooleanExpr expr)
    {
      final IRTempAddress result = new IRTempAddress();
      final boolean bool = expr.token().type() == TokenType.TRUE;
      final IRConstAddress rhs = new IRConstAddress(bool);
      irProgram.addQuad(new IRCopyInst(result, rhs));
      return result;
    }

    /**
     * Generates one or more IR instructions for the
     * given unary AST node.
     *
     * @param expr An unary AST node.
     * @return The destination address of the
     * result of the given AST node.
     */
    public IRAddress visit(UnaryExpr expr)
    {
      final TokenType tokType = expr.token().type();
      final IRTempAddress result = new IRTempAddress();
      final IRAddress arg = getDestinationAddr(expr.first());

      if (expr.token().type() == TokenType.MINUS && expr.first() instanceof IntegerExpr)
      {
        irProgram.addQuad(new IRCopyInst(result, arg));
      }
      else
      {
        irProgram.addQuad(new IRUnaryInst(tokType, arg, result));
      }

      return result;
    }

    /**
     * Generates one or more IR instructions for the
     * given arithmetic AST node.
     *
     * @param expr An arithmetic AST node.
     * @return The destination address of the
     * result of the arithmetic instruction.
     */
    public IRAddress visit(ArithmeticExpr expr)
    {
      final IRTempAddress result = new IRTempAddress();
      final TokenType tokType = expr.token().type();
      final IRAddress arg1 = getDestinationAddr(expr.first());
      final IRAddress arg2 = getDestinationAddr(expr.second());

      irProgram.addQuad(new IRBinaryInst(tokType, arg1, arg2, result));

      return result;
    }

    /**
     * Generates one or more IR instructions for the
     * given binary comparison AST node.
     *
     * @param expr A binary comparison expression.
     * @return The label of the false branch.
     */
    public IRAddress visit(CompareExpr expr)
    {
      final TokenType tokType = expr.token().type();
      final IRLabelAddress label = new IRLabelAddress();
      final IRTempAddress comparisonResult = new IRTempAddress();

      // Recursively generate IR instructions for the operands
      final IRAddress arg1 = getDestinationAddr(expr.first());
      final IRAddress arg2 = getDestinationAddr(expr.second());

      irProgram.addQuad(new IRCopyInst(comparisonResult, new IRConstAddress(0)));
      irProgram.addQuad(new IRIfInst(tokType, arg1, arg2, label));
      irProgram.addQuad(new IRCopyInst(comparisonResult, new IRConstAddress(1)));
      irProgram.addQuad(new IRLabelInst(label));

      return comparisonResult;
    }

    /**
     * Generates one or more IR instructions for the
     * given print expression.
     *
     * @param expr A print expression.
     * @return A null address, indicating that this expression
     * does not evaluate to a value.
     */
    public IRAddress visit(PrintExpr expr)
    {
      // Accumulate the param instructions to be
      // inserted directly before the call instruction.
      final List<Quadruple> params = new ArrayList<>();

      // Generate the instructions for the parameters.
      for (ASTNode child : expr.children())
      {
        IRAddress paramTemp = child.accept(this);
        params.add(new IRParamInst(paramTemp));
      }

      final IRNameAddress procName = new IRNameAddress(
        expr.token().rawText());
      final IRConstAddress numParams = new IRConstAddress(
        expr.children().size());

      irProgram.addQuads(params);
      irProgram.addQuad(new IRCallInst(procName, numParams));

      return new IRNullAddress();
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
    public IRAddress visit(LetExpr expr)
    {
      // Cache the previous environment and activate
      // the environment of this expression.
      final Env prevEnv = top;
      top = expr.environment();

      // Generate IR instructions for the expressions bounded
      // to the identifiers and populate the symbol table.
      ((LetBindings) expr.first()).accept(this);

      // Recursively generate IR instructions for the body expressions
      for (int i = 1; i < expr.children().size(); i++)
      {
        final Expr child = (Expr) expr.children().get(i);
        final IRAddress addr = child.accept(this);

        // Return the destination address of the last
        // expression of the body.
        if (i == expr.children().size() - 1)
          return addr;
      }

      // Restore the previous environment.
      top = prevEnv;

      return new IRNullAddress();
    }

    /**
     * Generates IR instructions for each LetBinding
     * AST node within the given LetBindings node.
     *
     * @param bindings A LetBindings AST node.
     * @return null.
     */
    public IRAddress visit(LetBindings bindings)
    {
      for (ASTNode n : bindings.children())
        ((LetBinding) n).accept(this);
      return null;
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
    public IRAddress visit(LetBinding binding)
    {
      final IRTempAddress result = new IRTempAddress();
      final String id = binding.first().token().rawText();

      // The source of the copy instruction is the
      // destination of the bounded expression.
      final IRAddress rhs = getDestinationAddr(binding.second());

      // Store the source address of the bounded expression
      // in the symbol table.
      top.get(id).setAddress(result);

      if (!(rhs instanceof IRNullAddress))
      {
        irProgram.addQuad(new IRCopyInst(result, rhs));
      }

      return null;
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
    public IRAddress visit(IdentifierExpr expr)
    {
      return top.get(expr.token().rawText()).address();
    }

    public IRAddress visit(IfExpr expr)
    {
      // Recursively generate IR instructions for the test condition
      IRAddress testResultAddr = expr.test().accept(this);

      if (testResultAddr instanceof IRNullAddress)
      {
        // The test condition does not evaluate to a value, so
        // we'll convert its NullAddress to a constant
        testResultAddr = ((IRNullAddress) testResultAddr).toIRConstantAddress();
      }

      // Jump to the else label if the test condition is false
      final IRLabelAddress elseLabel = new IRLabelAddress();

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

      final IRTempAddress branchResultAddr = new IRTempAddress();

      // Recursively generate IR instructions for the then branch
      final IRAddress consequentResultAddr = expr.consequent().accept(this);

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
      final IRLabelAddress doneLabel = new IRLabelAddress();
      irProgram.addQuad(new IRGotoInst(doneLabel));

      // Start of else block
      irProgram.addQuad(new IRLabelInst(elseLabel));

      if (expr instanceof IfThenElseExpr)
      {
        // Recursively generate an explicit else branch
        final IRAddress alternativeResultAddr = ((IfThenElseExpr) expr).alternative().accept(this);

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
        return branchResultAddr;
      } else
      {
        return new IRNullAddress();
      }
    }

    public IRAddress visit(IfThenElseExpr expr)
    {
      return visit((IfExpr)expr);
    }

    /*
     * Returns the destination address of the
     * result of the given expression.
     *
     * @param expr An expression.
     * @return Either a constant address, if the
     * expression is an integer, or a temporary address.
     */
    private IRAddress getDestinationAddr(Expr expr)
    {
      IRAddress addr = null;

      if (expr instanceof IntegerExpr)
      {
        long constant = Expr.isChildOfUnaryMinusExpr(expr)
          ? Long.parseLong(String.format("-%s", expr.token().rawText()))
          : ((IntegerExpr)expr).toConstant();

        addr = new IRConstAddress(constant);
      }
      else
      {
        addr = expr.accept(this);
      }

      return addr;
    }

    /*
     * Returns the destination address of the
     * result of the given AST node (presumably
     * an expression).
     *
     * @param n An AST node.
     * @return Either a constant address, if the
     * expression is an integer, or a temporary address.
     */
    private IRAddress getDestinationAddr(ASTNode n)
    {
      return getDestinationAddr((Expr) n);
    }
}
