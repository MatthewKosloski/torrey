package me.mtk.torrey.ir;

import java.util.List;
import java.util.ArrayList;
import me.mtk.torrey.ast.ASTNode;
import me.mtk.torrey.ast.ASTNodeVisitor;
import me.mtk.torrey.ast.BinaryExpr;
import me.mtk.torrey.ast.Expr;
import me.mtk.torrey.ast.IdentifierExpr;
import me.mtk.torrey.ast.IntegerExpr;
import me.mtk.torrey.ast.LetBinding;
import me.mtk.torrey.ast.LetBindings;
import me.mtk.torrey.ast.LetExpr;
import me.mtk.torrey.ast.PrintExpr;
import me.mtk.torrey.ast.Program;
import me.mtk.torrey.ast.UnaryExpr;
import me.mtk.torrey.symbols.Env;

public final class IRGenVisitor implements ASTNodeVisitor<TempAddress>
{
    // The IR Program being generated.
    private IRProgram irProgram;

    // The AST from which IR instructions will
    // be generated.
    private Program program;

    // The current environment.
    private Env top;

    public IRGenVisitor(Program program)
    {
        this.irProgram = new IRProgram();
        this.program = program;
    }

    public IRProgram gen()
    {
        program.accept(this);
        return irProgram;
    }

    /**
     * Traverses the given AST, generating intermediate code.
     * 
     * @param Program The root AST node.
     * @return The list of generated intermediate instructions.
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
     * given integer AST node.
     * 
     * @param expr An integer AST node.
     * @param result The address at which the value of 
     * the integer is to be stored.
     */
    public TempAddress visit(IntegerExpr expr)
    {
        final TempAddress result = new TempAddress();
        final ConstAddress constant = new ConstAddress(
            Integer.parseInt(expr.token().rawText()));
        irProgram.addQuad(new CopyInst(result, constant));
        return result;
    }

    /**
     * Generates one or more IR instructions for the 
     * given unary AST node.
     * 
     * @param expr An unary AST node.
     * @param result The address at which the result 
     * of the unary operation is to be stored.
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
     * @param expr An binary AST node.
     * @param result The address at which the result of 
     * the binary operation is to be stored.
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
     * Generates one or more IR instructions for 
     * the given print AST node.
     * 
     * @param expr A print AST node.
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
                
                // Return the destination address of the expression.
                if (i == expr.children().size() - 1) 
                    return addr;
            }
        }

        // Restore the previous environment.
        top = prevEnv;      

        return null;
    }

    public TempAddress visit(LetBindings bindings)
    {
        for (ASTNode n : bindings.children())
            ((LetBinding) n).accept(this);
        return null;
    }

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