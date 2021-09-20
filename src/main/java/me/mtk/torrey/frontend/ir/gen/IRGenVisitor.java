package me.mtk.torrey.frontend.ir.gen;

import java.util.*;
import me.mtk.torrey.frontend.ast.*;
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
public final class IRGenVisitor implements ASTNodeVisitor<IRAddress>
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
    public IRGenVisitor(Program program)
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

    /**
     * Generates one or more IR instructions for the 
     * given primitive AST node.
     * 
     * @param expr A primitive AST node.
     * @return The destination address of the 
     * result of the given AST node.
     */
    public IRAddress visit(PrimitiveExpr expr)
    {
        final IRTempAddress result = new IRTempAddress();

        IRConstAddress rhs;

        if (expr instanceof IntegerExpr)
            rhs = new IRConstAddress(expr.token().rawText());
        else if (expr instanceof BooleanExpr)
            rhs = new IRConstAddress(expr.token().type() 
                == TokenType.TRUE);
        else
        {
            throw new Error("IRGenVisitor.visit(PrimitiveExpr):"
            + " Unhandled primitive");
        }

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

        irProgram.addQuad(new IRUnaryInst(tokType, arg, result));

        return result;
    }

    /**
     * Generates one or more IR instructions for the 
     * given binary AST node.
     * 
     * @param expr A binary AST node.
     * @return The destination address of the 
     * result of the given AST node.
     */
    public IRAddress visit(BinaryExpr expr)
    {
        if (expr instanceof ArithmeticExpr)
            return visit((ArithmeticExpr) expr);
        else if (expr instanceof CompareExpr)
            return visit((CompareExpr) expr);

        return null;
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

        if (expr.getFold() != null)
        {
            // The binary expression can be reduced to a constant
            // expression, so create a constant address.
            int foldedConstant;
            if (expr.getFold() instanceof ConstantConvertable)
            {
                foldedConstant = ((ConstantConvertable) expr.getFold()).toConstant();
            }
            else
            {
                throw new Error(String.format("Unhandled expression type %s",
                    expr.getFold().getClass().getSimpleName()));
            }
            final IRConstAddress rhs = new IRConstAddress(foldedConstant);
            irProgram.addQuad(new IRCopyInst(result, rhs));
        }
        else 
        {
            // The binary expression cannot be reduced and thus
            // an arithmetic instruction must be emitted.
            final TokenType tokType = expr.token().type();
            final IRAddress arg1 = getDestinationAddr(expr.first());
            final IRAddress arg2 = getDestinationAddr(expr.second());
            irProgram.addQuad(new IRBinaryInst(tokType, arg1, arg2, result));
        }
        
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
        
        final IRAddress arg1 = getDestinationAddr(expr.first());
        final IRAddress arg2 = getDestinationAddr(expr.second());
        
        irProgram.addQuad(new IRIfInst(tokType, arg1, arg2, label));
        return label;
    }

    /**
     * Generates one or more IR instructions for the 
     * given print statement.
     * 
     * @param stmt A print statement.
     * @return null.
     */
    public IRAddress visit(PrintExpr stmt)
    {
        // Accumulate the param instructions to be
        // inserted directly before the call instruction.
        final List<IRQuadruple> params = new ArrayList<>();
                
        // Generate the instructions for the parameters.
        for (ASTNode child : stmt.children())
        {
            IRAddress paramTemp = child.accept(this);
            params.add(new IRParamInst(paramTemp));
        }
    
        final IRNameAddress procName = new IRNameAddress(
            stmt.token().rawText());
        final IRConstAddress numParams = new IRConstAddress(
            stmt.children().size());

        irProgram.addQuads(params);
        irProgram.addQuad(new IRCallInst(procName, numParams));

        return null;
    }

    /**
     * Generates one or more IR instructions for the 
     * given let expression.
     * 
     * @param expr A let expression.
     * @return If the let expression has one or more
     * expressions in its body, then the destination address 
     * of the last expression of the body is returned; null
     * otherwise.
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

        if (expr.children().size() > 1)
        {
            // The expression has one or more expressions
            // in its body, so generate IR instructions
            // for these expressions.

            for (int i = 1; i < expr.children().size(); i++)
            {
                final Expr child = (Expr) expr.children().get(i);
                final IRAddress addr = child.accept(this);
                
                // Return the destination address of the last
                // expression of the body.
                if (i == expr.children().size() - 1) 
                    return addr;
            }
        }

        // Restore the previous environment.
        top = prevEnv;      

        return null;
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

        irProgram.addQuad(new IRCopyInst(result, rhs));

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
        // Generate IR instructions for the test condition, 
        // returning the address of label of the alternate branch.
        IRLabelAddress doneLabel = null;
        if (expr.test() instanceof CompareExpr)
            // The test condition is a comparison, so generate
            // IR instructions for the comparison.
            doneLabel = (IRLabelAddress) expr.test().accept(this);
        else
        {
            // The test condition is not a comparison but rather
            // a primitve boolean, so generate an if-then instruction.
            final TokenType tokType = expr.test().token().type();
            doneLabel = new IRLabelAddress();
            irProgram.addQuad(new IRIfInst(
                new IRConstAddress(tokType != TokenType.TRUE),
                doneLabel));
        }
        
        // Generate IR instructions for the consequent branch.
        final IRAddress consequentBranchAddr = expr.consequent().accept(this);

        // The result of this if expression will be stored in this temp.
        final IRTempAddress result = new IRTempAddress();

        // If we have an address of the last expression in the
        // consequent branch, then store it in the temp.
        if (consequentBranchAddr != null)
            irProgram.addQuad(new IRCopyInst(result, consequentBranchAddr));

        // Finally, generate the done label instruction.
        irProgram.addQuad(new IRLabelInst(doneLabel));

        return result;
    }

    public IRAddress visit(IfThenElseExpr expr)
    {
        // Generate IR instructions for the test condition, 
        // returning the address of label of the alternate branch.
        IRLabelAddress altBranchLabel = null;
        if (expr.test() instanceof CompareExpr)
            // The test condition is a comparison, so generate
            // IR instructions for the comparison.
            altBranchLabel = (IRLabelAddress) expr.test().accept(this);
        else
        {
            // The test condition is not a comparison but rather
            // a primitve boolean, so generate an if-then instruction.
            final TokenType tokType = expr.test().token().type();
            altBranchLabel = new IRLabelAddress();
            irProgram.addQuad(new IRIfInst(
                new IRConstAddress(tokType != TokenType.TRUE),
                altBranchLabel));
        }
        
        // Generate IR instructions for the consequent branch.
        final IRAddress consequentBranchAddr = expr.consequent().accept(this);

        // The result of this if expression will be stored in this temp.
        final IRTempAddress result = new IRTempAddress();

        // If we have an address of the last expression in the
        // consequent branch, then store it in the temp.
        if (consequentBranchAddr != null)
            irProgram.addQuad(new IRCopyInst(result, consequentBranchAddr));

        // Generate IR instructions for the alternative branch.

        // Generate a new label and go to that label if
        // the test condition is true.
        final IRLabelAddress doneLabel = new IRLabelAddress();
        irProgram.addQuad(new IRGotoInst(doneLabel));

        irProgram.addQuad(new IRLabelInst((IRLabelAddress) altBranchLabel));
        final IRAddress alternativeBranchAddr = expr.alternative().accept(this);
        
        // If we have an address of the last expression in the
        // alternative branch, then store it in the temp.
        if (alternativeBranchAddr != null)
            irProgram.addQuad(new IRCopyInst(result, 
                alternativeBranchAddr));

        // Finally, generate the done label instruction.
        irProgram.addQuad(new IRLabelInst((IRLabelAddress) doneLabel));

        return result;
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
            addr = new IRConstAddress(expr.token().rawText());
        else
            addr = expr.accept(this);
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