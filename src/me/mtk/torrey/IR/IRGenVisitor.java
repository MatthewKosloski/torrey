// package me.mtk.torrey.IR;

// import java.util.List;
// import java.util.ArrayList;
// import java.util.Stack;
// import me.mtk.torrey.ErrorReporter.ErrorMessages;
// import me.mtk.torrey.ErrorReporter.ErrorReporter;
// import me.mtk.torrey.ErrorReporter.SemanticError;
// import me.mtk.torrey.AST.ExprVisitor;
// import me.mtk.torrey.AST.ASTNode;
// import me.mtk.torrey.AST.BinaryExpr;
// import me.mtk.torrey.AST.IntegerExpr;
// import me.mtk.torrey.AST.PrintExpr;
// import me.mtk.torrey.AST.UnaryExpr;
// import me.mtk.torrey.AST.Expr;
// import me.mtk.torrey.AST.Program;
// import me.mtk.torrey.AST.ProgramVisitor;
// import me.mtk.torrey.Lexer.Token;
// import me.mtk.torrey.Analysis.DataType;

// /**
//  * Generates an intermediate representation (IR).
//  */
// public class IRGenVisitor implements 
//     ExprVisitor<IRInst>, ProgramVisitor<List<IRInst>>
// {

//     // current temp var number
//     private int temp;
//     // accumulated list of instructions
//     private List<IRInst> instrs;

//     private Stack<Integer> temps;

//     public List<IRInst> visit(Program p)
//     {
//         for (ASTNode child : p.children())
//             ((Expr) child).accept(this);

//         return instrs;
//     }

//     public IRInst visit(BinaryExpr exp)
//     {
        
//     }

//     public IRInst visit(PrintExpr exp)
//     {
        
//     }

//     public IRInst visit(Integer exp)
//     {
        
//     }

//     public IRInst visit(UnaryExpr exp)
//     {

//     }

//     private int newtemp()
//     {
//         return temp++;
//     }
// }
