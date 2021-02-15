package me.mtk.torrey.ast;


import org.json.JSONArray;
import org.json.JSONObject;
import me.mtk.torrey.lexer.Token;
import me.mtk.torrey.lexer.Position;

public final class PrettyPrinterVisitor implements 
    ExprVisitor<Object>, ProgramVisitor<Object>
{
    public String visit(Program program)
    {
        final JSONObject jo = new JSONObject();

        final JSONArray ja = new JSONArray();
        for (ASTNode child : program.children())
            ja.put(((Expr) child).accept(this));

        jo.put("node_type", "Program")
            .put("children", ja)
            .put("token", prettyToken(program.token()));

        return jo.toString(2);
    }

    public JSONObject visit(BinaryExpr expr)
    {
        final JSONObject jo = new JSONObject();
        final JSONArray ja = new JSONArray();

        ja.put(((Expr) expr.first()).accept(this));
        ja.put(((Expr) expr.second()).accept(this));

        jo.put("node_type", "BinaryExpr")
            .put("children", ja)
            .put("token", prettyToken(expr.token()));

        return jo;
    }

    public JSONObject visit(UnaryExpr expr)
    {
        final JSONObject jo = new JSONObject();

        jo.put("node_type", "UnaryExpr")
            .put("child", ((Expr) expr.first()).accept(this))
            .put("token", prettyToken(expr.token()));

        return jo;
    }

    public JSONObject visit(PrintExpr expr)
    {
        final JSONObject jo = new JSONObject();
        final JSONArray ja = new JSONArray();

        for (ASTNode child : expr.children())
            ja.put(((Expr) child).accept(this));

        jo.put("node_type", "PrintExpr")
            .put("children", ja)
            .put("token", prettyToken(expr.token()));

        return jo;
    }

    public JSONObject visit(IntegerExpr expr)
    {
        final JSONObject jo = new JSONObject()
            .put("node_type", "IntegerExpr")
            .put("token", prettyToken(expr.token()));

        return jo;
    }


    public JSONObject prettyToken(Token tok)
    {
        final JSONObject jo = new JSONObject()
            .put("rawText", tok.rawText())
            .put("type", tok.type())
            .put("startPos", tok.startPos() != null 
                ? prettyPos(tok.startPos()) 
                : "null")
            .put("endPos", tok.startPos() != null 
                ? prettyPos(tok.endPos()) 
                : "null")
            .put("beginIndex", tok.beginIndex())
            .put("beginLineIndex", tok.beginLineIndex())
            .put("endIndex", tok.endIndex());

        return jo;
    }

    public JSONObject prettyPos(Position pos)
    {
        final JSONObject jo = new JSONObject()
            .put("line", pos.line())
            .put("col", pos.col());

        return jo;
    }


}
