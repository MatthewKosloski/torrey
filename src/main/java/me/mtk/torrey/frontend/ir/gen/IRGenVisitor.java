package me.mtk.torrey.frontend.ir.gen;

import java.util.List;
import java.util.ArrayList;
import me.mtk.torrey.frontend.ast.ASTNode;
import me.mtk.torrey.frontend.ast.ASTNodeVisitor;
import me.mtk.torrey.frontend.ast.BinaryExpr;
import me.mtk.torrey.frontend.ast.BooleanExpr;
import me.mtk.torrey.frontend.ast.Expr;
import me.mtk.torrey.frontend.ast.IdentifierExpr;
import me.mtk.torrey.frontend.ast.IntegerExpr;
import me.mtk.torrey.frontend.ast.LetBinding;
import me.mtk.torrey.frontend.ast.LetBindings;
import me.mtk.torrey.frontend.ast.LetExpr;
import me.mtk.torrey.frontend.ast.PrimitiveExpr;
import me.mtk.torrey.frontend.ast.PrintExpr;
import me.mtk.torrey.frontend.ast.Program;
import me.mtk.torrey.frontend.ast.UnaryExpr;
import me.mtk.torrey.frontend.symbols.Env;
import me.mtk.torrey.frontend.ir.addressing.Address;
import me.mtk.torrey.frontend.ir.addressing.TempAddress;
import me.mtk.torrey.frontend.ir.addressing.ConstAddress;
import me.mtk.torrey.frontend.ir.addressing.NameAddress;
import me.mtk.torrey.frontend.ir.instructions.BinaryInst;
import me.mtk.torrey.frontend.ir.instructions.Quadruple;
import me.mtk.torrey.frontend.ir.instructions.CallInst;
import me.mtk.torrey.frontend.ir.instructions.CopyInst;
import me.mtk.torrey.frontend.ir.instructions.ParamInst;
import me.mtk.torrey.frontend.ir.instructions.UnaryInst;
import me.mtk.torrey.frontend.ir.instructions.UnaryOpType;
import me.mtk.torrey.frontend.ir.instructions.BinaryOpType;

/**
 * Converts the given abstract syntax tree to a more low-level,
 * linear intermediate representation, or IR. This IR is a type
 * of three-address code represented as a collection of quadruples
 * of the form (operator, argument1, argument2, result).
 */
public final class IRGenVisitor implements ASTNodeVisitor<TempAddress>
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
    public TempAddress visit(Program program)
    {
        // For every AST node, call the appropriate
        // visit() method to generate the IR instruction
        // corresponding to that node.
        for (ASTNode child : program.children())
            ((Expr) child).accept(this);

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
    public TempAddress visit(PrimitiveExpr expr)
    {
        final TempAddress result = new TempAddress();

        ConstAddress rhs;

        if (expr instanceof IntegerExpr)
            rhs = new ConstAddress(expr.token().rawText());
        else if (expr instanceof BooleanExpr)
        {
            final String rawText = expr.token().rawText();
            final boolean constant = rawText.equals("true");            
            rhs = new ConstAddress(constant);
        }
        else
        {
            throw new Error("IRGenVisitor.visit(PrimitiveExpr):"
            + " Unhandled primitive");
        }

        irProgram.addQuad(new CopyInst(result, rhs));

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
    public TempAddress visit(UnaryExpr expr)
    {
        final TempAddress result = new TempAddress();
        final UnaryOpType op = UnaryOpType.transUnaryOp(
            expr.token().rawText());

        final Address arg = getDestinationAddr(expr.first());

        irProgram.addQuad(new UnaryInst(op, arg, result));

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
    public TempAddress visit(BinaryExpr expr)
    {
        final TempAddress result = new TempAddress();
        final BinaryOpType op = BinaryOpType.transBinaryOp(
            expr.token().rawText());
        
        final Address arg1 = getDestinationAddr(expr.first());
        final Address arg2 = getDestinationAddr(expr.second());

        irProgram.addQuad(new BinaryInst(op, arg1, arg2, result));

        return result;
    }

    /**
     * Generates one or more IR instructions for the 
     * given print expression.
     * 
     * @param expr An print expression.
     * @return The destination address of the 
     * result of the given print expression.
     */
    public TempAddress visit(PrintExpr expr)
    {
        // Accumulate the param instructions to be
        // inserted directly before the call instruction.
        final List<Quadruple> params = new ArrayList<>();
                
        // Generate the instructions for the parameters.
        for (ASTNode child : expr.children())
        {
            TempAddress paramTemp = child.accept(this);
            params.add(new ParamInst(paramTemp));
        }
    
        final NameAddress procName = new NameAddress(
            expr.token().rawText());
        final ConstAddress numParams = new ConstAddress(
            expr.children().size());

        irProgram.addQuads(params);
        irProgram.addQuad(new CallInst(procName, numParams));

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
    public TempAddress visit(LetExpr expr)
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
                final TempAddress addr = child.accept(this);
                
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
    public TempAddress visit(LetBindings bindings)
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
    public TempAddress visit(LetBinding binding)
    {
        final TempAddress result = new TempAddress();
        final String id = binding.first().token().rawText();

        // The source of the copy instruction is the
        // destination of the bounded expression.
        final Address rhs = getDestinationAddr(binding.second());

        // Store the source address of the bounded expression
        // in the symbol table.
        top.get(id).setAddress(result);

        irProgram.addQuad(new CopyInst(result, rhs));

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
    public TempAddress visit(IdentifierExpr expr)
    {
        return top.get(expr.token().rawText()).address();
    }

    /*
     * Returns the destination address of the 
     * result of the given expression.
     * 
     * @param expr An expression.
     * @return Either a constant address, if the 
     * expression is an integer, or a temporary address.
     */
    private Address getDestinationAddr(Expr expr)
    {
        Address addr = null;
        if (expr instanceof IntegerExpr)
            addr = new ConstAddress(expr.token().rawText());
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
    private Address getDestinationAddr(ASTNode n)
    {
        return getDestinationAddr((Expr) n);
    }
}