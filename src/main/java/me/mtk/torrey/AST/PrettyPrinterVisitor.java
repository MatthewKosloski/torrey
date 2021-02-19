package me.mtk.torrey.ast;


import org.json.JSONArray;
import org.json.JSONObject;
import me.mtk.torrey.lexer.Token;
import me.mtk.torrey.lexer.Position;

public final class PrettyPrinterVisitor implements ASTNodeVisitor<Object>
{
    public String visit(Program program)
    {
        final JSONObject jo = new JSONObject();

        final JSONArray ja = new JSONArray();
        for (ASTNode child : program.children())
            ja.put(child.accept(this));

        jo.put("node_type", "Program")
            .put("children", ja)
            .put("token", prettyToken(program.token()));

        return jo.toString(2);
    }

    public Object visit(LetExpr expr) {
        final JSONObject jo = new JSONObject();
        final JSONArray ja = new JSONArray();

        for (ASTNode child : expr.children())
            ja.put(child.accept(this));

        jo.put("node_type", "LetExpr")
            .put("children", ja)
            .put("token", prettyToken(expr.token()));

        return jo;
    }

    public Object visit(LetBinding binding)
    {
        final JSONObject jo = new JSONObject();
        final JSONArray ja = new JSONArray();

        for (ASTNode child : binding.children())
            ja.put(child.accept(this));

        jo.put("node_type", "LetBinding")
            .put("children", ja)
            .put("token", prettyToken(binding.token()));

        return jo;
    }

    public Object visit(LetBindings bindings)
    {
        final JSONObject jo = new JSONObject();
        final JSONArray ja = new JSONArray();

        for (ASTNode child : bindings.children())
            ja.put(child.accept(this));

        jo.put("node_type", "LetBindings")
            .put("children", ja)
            .put("token", bindings.token() == null 
                ? "null" 
                : bindings.token());

        return jo;
    }

    public Object visit(IdentifierExpr expr) 
    {
        final JSONObject jo = new JSONObject()
            .put("node_type", "IdentifierExpr")
            .put("token", prettyToken(expr.token()));

        return jo;
    }

    public JSONObject visit(BinaryExpr expr)
    {
        final JSONObject jo = new JSONObject();
        final JSONArray ja = new JSONArray();

        ja.put(expr.first().accept(this));
        ja.put(expr.second().accept(this));

        jo.put("node_type", "BinaryExpr")
            .put("children", ja)
            .put("token", prettyToken(expr.token()));

        return jo;
    }

    public JSONObject visit(UnaryExpr expr)
    {
        final JSONObject jo = new JSONObject();

        jo.put("node_type", "UnaryExpr")
            .put("child", expr.first().accept(this))
            .put("token", prettyToken(expr.token()));

        return jo;
    }

    public JSONObject visit(PrintExpr expr)
    {
        final JSONObject jo = new JSONObject();
        final JSONArray ja = new JSONArray();

        for (ASTNode child : expr.children())
            ja.put(child.accept(this));

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


    private JSONObject prettyToken(Token tok)
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

    private JSONObject prettyPos(Position pos)
    {
        final JSONObject jo = new JSONObject()
            .put("line", pos.line())
            .put("col", pos.col());

        return jo;
    }
}
