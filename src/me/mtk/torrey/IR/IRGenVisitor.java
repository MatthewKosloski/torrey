package me.mtk.torrey.IR;

import java.util.List;
import java.util.ArrayList;
import me.mtk.torrey.AST.ASTNode;
import me.mtk.torrey.AST.BinaryExpr;
import me.mtk.torrey.AST.Expr;
import me.mtk.torrey.AST.ExprIRVisitor;
import me.mtk.torrey.AST.IntegerExpr;
import me.mtk.torrey.AST.PrintExpr;
import me.mtk.torrey.AST.Program;
import me.mtk.torrey.AST.UnaryExpr;

public final class IRGenVisitor extends IRGenerator implements 
    ExprIRVisitor<Void>
{

    public IRGenVisitor(Program program)
    {
        super(program);
    }

    /**
     * Traverses the given AST, generating intermediate code.
     * 
     * @param Program The root AST node.
     * @return The list of generated intermediate instructions.
     */
    public IRProgram gen()
    {
        // For every AST node, call the appropriate
        // visit() method to generate the IR instruction
        // corresponding to that node.
        for (ASTNode child : program.children())
            ((Expr) child).accept(this, newTemp());
    
        return irProgram;
    }

    /**
     * Generates one or more IR instructions for the given integer AST node.
     * 
     * @param expr An integer AST node.
     * @param result The address at which the value of the integer
     * is to be stored.
     */
    public Void visit(IntegerExpr expr, TempAddress result)
    {
        final ConstAddress constant = new ConstAddress(
            Integer.parseInt(expr.token().rawText()));
        irProgram.addQuad(new CopyInst(result, constant));
        irProgram.define(result.value(), constant.getConstant());

        return null;
    }

    /**
     * Generates one or more IR instructions for the given unary AST node.
     * 
     * @param expr An unary AST node.
     * @param result The address at which the result of the unary operation
     * is to be stored.
     */
    public Void visit(UnaryExpr expr, TempAddress result)
    {
        final UnaryOpType op = transUnaryOp(expr.token().rawText());
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
            arg = newTemp();
            childExpr.accept(this, (TempAddress) arg);
        }

        irProgram.addQuad(new UnaryInst(op, arg, result));

        return null;
    }

    /**
     * Generates one or more IR instructions for the given binary AST node.
     * 
     * @param expr An binary AST node.
     * @param result The address at which the result of the binary operation
     * is to be stored.
     */
    public Void visit(BinaryExpr expr, TempAddress result)
    {
        final BinaryOpType op = transBinaryOp(expr.token().rawText());

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
            arg1 = newTemp();
            first.accept(this, (TempAddress) arg1);
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
            arg2 = newTemp();
            second.accept(this, (TempAddress) arg2);
        }

        irProgram.addQuad(new BinaryInst(op, arg1, arg2, result));

        return null;
    }

    /**
     * Generates one or more IR instructions for the given print AST node.
     * 
     * @param expr A print AST node.
     */
    public Void visit(PrintExpr expr)
    {
        // Accumulate the param instructions to be
        // inserted directly before the call instruction.
        final List<Quadruple> params = new ArrayList<>();
                
        // Generate the instructions for the parameters.
        for (ASTNode child : expr.children())
        {
            final Expr childExpr = (Expr) child;
            if (childExpr instanceof IntegerExpr)
                // The parameter is an integer,
                // so rather than generating a temp address,
                // just generate a constant address.
                params.add(new ParamInst(
                    new ConstAddress(childExpr.token().rawText())));
            else
            {
                // The parameter is a more complex sub-expression,
                // so generate a temp to store the result of
                // the complex sub-expression.
                final TempAddress paramTemp = newTemp();
                childExpr.accept(this, paramTemp);
                params.add(new ParamInst(paramTemp));
            }
        }
    
        final NameAddress procName = new NameAddress(expr.token().rawText());
        final ConstAddress numParams = new ConstAddress(expr.children().size());

        irProgram.addQuads(params);
        irProgram.addQuad(new CallInst(procName, numParams));

        return null;
    }
}