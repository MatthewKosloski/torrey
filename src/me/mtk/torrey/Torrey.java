package me.mtk.torrey;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import java.util.ArrayList;

import me.mtk.torrey.Lexer.*;
import me.mtk.torrey.Parser.*;
import me.mtk.torrey.AST.*;

public class Torrey 
{
    public static void main(String[] args)
    {
        if (args.length > 1) 
        {
            System.out.println("Usage: Torrey [script]");
            System.exit(64);
        }

        try
        {
            final String input = read(args[0]);
            final TorreyLexer lexer = new TorreyLexer(input);
            final List<Token> tokens = lexer.start();
            final Grammar grammar = new Grammar(tokens);
            final List<ExprNode> exprs = grammar.program();
            System.out.println(exprs);

            // (+ 2 3)
            // IntegerExprNode first = new IntegerExprNode(tokens.get(2));
            // IntegerExprNode second = new IntegerExprNode(tokens.get(3));
            // BinaryExprNode root = new BinaryExprNode(tokens.get(1), first, second);

            // (println
            //    (+ 25 5)
            //    (/ (* 6 2) 3))
            // List<ExprNode> exprList = new ArrayList<>();
            // exprList.add(new BinaryExprNode(tokens.get(3), 
            //     new IntegerExprNode(tokens.get(4)),
            //     new IntegerExprNode(tokens.get(5))));
            // exprList.add(new BinaryExprNode(tokens.get(8), 
            //     new BinaryExprNode(tokens.get(10),
            //         new IntegerExprNode(tokens.get(11)),
            //         new IntegerExprNode(tokens.get(12))),
            //     new IntegerExprNode(tokens.get(14))));
            // PrintExprNode root = new PrintExprNode(tokens.get(1), exprList);
        }
        catch (IOException e)
        {
            System.err.println("Encountered an I/O error.");
        }
    }

    /**
     * Reads the contents of the file at the given path, 
     * returning the contents of the file as a string object.
     * 
     * @param path Path to the file on the file system.
     * @return A string containing the contents of the file.
     * @throws IOException If an I/O error occurs.
     */
    public static String read(final String path) throws IOException
    {
        final byte[] bytes = Files.readAllBytes(Paths.get(path));   
        return new String(bytes, Charset.defaultCharset());
    }
}
