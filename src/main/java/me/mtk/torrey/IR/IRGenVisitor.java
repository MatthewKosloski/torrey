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
        final Expr childExpr = (Expr) expr.first();

        // This will either be a constant or temporary.
        Address arg;

        if (childExpr instanceof IntegerExpr)
            // The argument is an integer,
            // so rather than generating a temp address,
            // just generate a constant address.
            arg = new ConstAddress(childExpr.token().rawText());
        else
        {
            // The argument is a more complex sub-expression,
            // so generate a temp to store the result of
            // the complex sub-expression.
            arg = childExpr.accept(this);
        }

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

        final Expr first = (Expr) expr.first();
        final Expr second = (Expr) expr.second();
        
        // These will either be a constant or temporary.
        Address arg1, arg2;

        if (first instanceof IntegerExpr)
            // The argument is an integer,
            // so rather than generating a temp address,
            // just generate a constant address.
            arg1 = new ConstAddress(first.token().rawText());
        else
        {
            // The argument is a more complex sub-expression,
            // so generate a temp to store the result of
            // the complex sub-expression.
            arg1 = first.accept(this);
        }

        if (second instanceof IntegerExpr)
            // The argument is an integer,
            // so rather than generating a temp address,
            // just generate a constant address.
            arg2 = new ConstAddress(second.token().rawText());
        else
        {
            // The argument is a more complex sub-expression,
            // so generate a temp to store the result of
            // the complex sub-expression.
            arg2 = second.accept(this);
        }

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
        final IdentifierExpr idExpr = (IdentifierExpr) binding.first();
        final Expr expr = (Expr) binding.second();
        final String id = idExpr.token().rawText();

        // An address to store the result of expr.
        Address rhs = null;

        if (expr instanceof IntegerExpr)
            // The argument is an integer,
            // so rather than generating a temp address,
            // just generate a constant address.
            rhs = new ConstAddress(expr.token().rawText());
        else
        {
            // The argument is a more complex sub-expression,
            // so generate a temp to store the result of
            // the complex sub-expression.

            // Generate IR instructions for the bounded expression.
            rhs = expr.accept(this);
        }

        // Store the temp address in the symbol table.
        top.get(id).setAddress(result);

        irProgram.addQuad(new CopyInst(result, rhs));

        return null;
    }

    public TempAddress visit(IdentifierExpr expr)
    {
        return top.get(expr.token().rawText()).address();
    }
}