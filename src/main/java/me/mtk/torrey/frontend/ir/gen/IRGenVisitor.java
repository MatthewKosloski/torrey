package me.mtk.torrey.frontend.ir.gen;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import me.mtk.torrey.frontend.ast.ASTNode;
import me.mtk.torrey.frontend.ast.ASTNodeVisitor;
import me.mtk.torrey.frontend.ast.ArithmeticExpr;
import me.mtk.torrey.frontend.ast.BinaryExpr;
import me.mtk.torrey.frontend.ast.BooleanExpr;
import me.mtk.torrey.frontend.ast.CompareExpr;
import me.mtk.torrey.frontend.ast.Expr;
import me.mtk.torrey.frontend.ast.IdentifierExpr;
import me.mtk.torrey.frontend.ast.IfExpr;
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
import me.mtk.torrey.frontend.ir.addressing.LabelAddress;
import me.mtk.torrey.frontend.ir.addressing.NameAddress;
import me.mtk.torrey.frontend.ir.instructions.BinaryInst;
import me.mtk.torrey.frontend.ir.instructions.Quadruple;
import me.mtk.torrey.frontend.ir.instructions.CallInst;
import me.mtk.torrey.frontend.ir.instructions.CopyInst;
import me.mtk.torrey.frontend.ir.instructions.GotoInst;
import me.mtk.torrey.frontend.ir.instructions.IfInst;
import me.mtk.torrey.frontend.ir.instructions.LabelInst;
import me.mtk.torrey.frontend.ir.instructions.ParamInst;
import me.mtk.torrey.frontend.ir.instructions.UnaryInst;
import me.mtk.torrey.frontend.ir.instructions.UnaryOpType;
import me.mtk.torrey.frontend.lexer.TokenType;
import me.mtk.torrey.frontend.ir.instructions.BinaryOpType;

/**
 * Converts the given abstract syntax tree to a more low-level,
 * linear intermediate representation, or IR. This IR is a type
 * of three-address code represented as a collection of quadruples
 * of the form (operator, argument1, argument2, result).
 */
public final class IRGenVisitor implements ASTNodeVisitor<Address>
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
    public Address visit(Program program)
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
    public Address visit(PrimitiveExpr expr)
    {
        final TempAddress result = new TempAddress();

        ConstAddress rhs;

        if (expr instanceof IntegerExpr)
            rhs = new ConstAddress(expr.token().rawText());
        else if (expr instanceof BooleanExpr)
            rhs = new ConstAddress(expr.token().type() 
                == TokenType.TRUE);
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
    public Address visit(UnaryExpr expr)
    {
        final TempAddress result = new TempAddress();
        final Optional<UnaryOpType> op = UnaryOpType.getUnaryOpType(
            expr.token().type());

        final Address arg = getDestinationAddr(expr.first());

        irProgram.addQuad(new UnaryInst(op.get(), arg, result));

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
    public Address visit(BinaryExpr expr)
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
    public Address visit(ArithmeticExpr expr)
    {
        final TempAddress result = new TempAddress();
        final Optional<BinaryOpType> op = BinaryOpType.getBinaryOpType(
            expr.token().type());

        if (expr.getFold() != null)
        {
            // The binary expression can be reduced to a constant
            // expression, so create a constant address.
            final String foldedConstant = expr.getFold().token().rawText();
            final ConstAddress rhs = new ConstAddress(foldedConstant);
            irProgram.addQuad(new CopyInst(result, rhs));
        }
        else 
        {
            // The binary expression cannot be reduced and thus
            // an arithmetic instruction must be emitted.
            final Address arg1 = getDestinationAddr(expr.first());
            final Address arg2 = getDestinationAddr(expr.second());
            irProgram.addQuad(new BinaryInst(op.get(), arg1, arg2, result));
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
    public Address visit(CompareExpr expr)
    {
        final LabelAddress label = new LabelAddress();
        
        final Address arg1 = getDestinationAddr(expr.first());
        final Address arg2 = getDestinationAddr(expr.second());
        
        final TokenType tokType = expr.token().type();
        final Optional<BinaryOpType> irOpType = BinaryOpType.getBinaryOpType(
            tokType);

        // Only go to label if condition is false, so we 
        // negate the condition.
        final Optional<BinaryOpType> negatedIrOpType = BinaryOpType.negate(
            irOpType.get());

        irProgram.addQuad(new IfInst(negatedIrOpType.get(), arg1, 
            arg2, label));
        return label;
    }

    /**
     * Generates one or more IR instructions for the 
     * given print statement.
     * 
     * @param stmt A print statement.
     * @return null.
     */
    public Address visit(PrintExpr stmt)
    {
        // Accumulate the param instructions to be
        // inserted directly before the call instruction.
        final List<Quadruple> params = new ArrayList<>();
                
        // Generate the instructions for the parameters.
        for (ASTNode child : stmt.children())
        {
            Address paramTemp = child.accept(this);
            params.add(new ParamInst(paramTemp));
        }
    
        final NameAddress procName = new NameAddress(
            stmt.token().rawText());
        final ConstAddress numParams = new ConstAddress(
            stmt.children().size());

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
    public Address visit(LetExpr expr)
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
                final Address addr = child.accept(this);
                
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
    public Address visit(LetBindings bindings)
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
    public Address visit(LetBinding binding)
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
    public Address visit(IdentifierExpr expr)
    {
        return top.get(expr.token().rawText()).address();
    }

    public Address visit(IfExpr expr)
    {
        // Generate IR instructions for the test condition, 
        // returning the address of label of the alternate branch.
        LabelAddress altBranchLabel = null;
        if (expr.test() instanceof CompareExpr)
            // The test condition is a comparison, so generate
            // IR instructions for the comparison.
            altBranchLabel = (LabelAddress) expr.test().accept(this);
        else
        {
            // The test condition is not a comparison but rather
            // a primitve boolean, so generate an if-then instruction.
            final TokenType tokType = expr.test().token().type();
            altBranchLabel = new LabelAddress();
            irProgram.addQuad(new IfInst(
                new ConstAddress(tokType != TokenType.TRUE),
                altBranchLabel));
        }
        
        // Generate IR instructions for the consequent branch.
        final Address consequentBranchAddr = expr.consequent().accept(this);

        // The result of this if expression will be stored in this temp.
        final TempAddress result = new TempAddress();

        // If we have an address of the last expression in the
        // consequent branch, then store it in the temp.
        if (consequentBranchAddr != null)
            irProgram.addQuad(new CopyInst(result, consequentBranchAddr));

        // Generate IR instructions for the alternative branch.
        LabelAddress doneLabel;
        if (expr.alternative() != null)
        {
            // Generate a new label and go to that label if
            // the test condition is true.
            doneLabel = new LabelAddress();
            irProgram.addQuad(new GotoInst(doneLabel));

            irProgram.addQuad(new LabelInst((LabelAddress) altBranchLabel));
            final Address alternativeBranchAddr = expr.alternative()
                .accept(this);
            
            // If we have an address of the last expression in the
            // alternative branch, then store it in the temp.
            if (alternativeBranchAddr != null)
                irProgram.addQuad(new CopyInst(result, 
                    alternativeBranchAddr));
        }
        else
            // There is no alternative branch, so make the
            // done label the same as the label generated
            // by the if instruction.
            doneLabel = altBranchLabel;

        // Finally, generate the done label instruction.
        irProgram.addQuad(new LabelInst((LabelAddress) doneLabel));

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