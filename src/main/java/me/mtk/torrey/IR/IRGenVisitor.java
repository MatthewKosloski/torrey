package me.mtk.torrey.ir;

import java.util.List;
import java.util.Stack;
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

public final class IRGenVisitor implements ASTNodeVisitor<Object>
{
    // The current temp variable number.
    private int tempCounter;

    // The IR Program being generated.
    private IRProgram irProgram;

    // Holds the temporary address to be used.
    private Stack<TempAddress> temps;

    // The AST from which IR instructions will
    // be generated.
    private Program program;

    public IRGenVisitor(Program program)
    {
        this.irProgram = new IRProgram();
        this.temps = new Stack<>();
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
    public Void visit(Program program)
    {
        // For every AST node, call the appropriate
        // visit() method to generate the IR instruction
        // corresponding to that node.
        for (ASTNode child : program.children())
        {
            // Push new temporary address onto the stack
            // to store the result of the expression.
            temps.push(newTemp());
            ((Expr) child).accept(this);
        }

        return null;
    }

    /**
     * Generates one or more IR instructions for the given integer AST node.
     * 
     * @param expr An integer AST node.
     * @param result The address at which the value of the integer
     * is to be stored.
     */
    public Void visit(IntegerExpr expr)
    {
        final TempAddress result = temps.pop();
        final ConstAddress constant = new ConstAddress(
            Integer.parseInt(expr.token().rawText()));
        irProgram.addQuad(new CopyInst(result, constant));

        return null;
    }

    /**
     * Generates one or more IR instructions for the given unary AST node.
     * 
     * @param expr An unary AST node.
     * @param result The address at which the result of the unary operation
     * is to be stored.
     */
    public Void visit(UnaryExpr expr)
    {
        final TempAddress result = temps.pop();
        final UnaryOpType op = UnaryOpType.transUnaryOp(expr.token().rawText());
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
            temps.push(newTemp());
            arg = temps.peek();
            childExpr.accept(this);
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
    public Void visit(BinaryExpr expr)
    {
        final TempAddress result = temps.pop();
        final BinaryOpType op = BinaryOpType.transBinaryOp(expr.token().rawText());

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
            temps.push(newTemp());
            arg1 = temps.peek();
            first.accept(this);
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
            temps.push(newTemp());
            arg2 = temps.peek();
            second.accept(this);
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
            temps.push(newTemp());
            final TempAddress paramTemp = temps.peek();
            ((Expr) child).accept(this);
            params.add(new ParamInst(paramTemp));
        }
    
        final NameAddress procName = new NameAddress(expr.token().rawText());
        final ConstAddress numParams = new ConstAddress(expr.children().size());

        irProgram.addQuads(params);
        irProgram.addQuad(new CallInst(procName, numParams));

        return null;
    }

    public Void visit(LetExpr expr)
    {
        return null;
    }

    public Void visit(LetBindings bindings)
    {
        return null;
    }

    public Void visit(LetBinding binding)
    {
        return null;
    }

    public Void visit(IdentifierExpr expr)
    {
        return null;
    }


    /**
     * Generates a new temp address.
     * 
     * @return A temporary address.
     */
    private TempAddress newTemp()
    {
        return new TempAddress(String.format("t%d", tempCounter++));
    }
}