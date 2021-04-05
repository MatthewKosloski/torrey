package me.mtk.torrey.frontend.ast;

import org.json.JSONArray;
import org.json.JSONObject;
import me.mtk.torrey.frontend.lexer.Token;
import me.mtk.torrey.frontend.lexer.Position;
import me.mtk.torrey.frontend.symbols.Env;
import me.mtk.torrey.frontend.symbols.Symbol;

/**
 * Pretty prints an AST by producing a JSON representation.
 */
public final class PrettyPrinterVisitor implements ASTNodeVisitor<Object>
{
    /**
     * Returns a JSON representation of the given AST.
     * @param program The root node of an AST.
     */
    public String visit(Program program)
    {
        return parse(program).toString(2);
    }

    public JSONObject visit(LetExpr expr) { return parse(expr); }
    public JSONObject visit(LetBinding binding) { return parse(binding); }
    public JSONObject visit(LetBindings bindings) { return parse(bindings); }
    public JSONObject visit(IdentifierExpr expr) { return parse(expr); }
    public JSONObject visit(BinaryExpr expr) { return parse(expr); }
    public JSONObject visit(CompareExpr expr) { return parse(expr); }
    public JSONObject visit(UnaryExpr expr) { return parse(expr); }
    public JSONObject visit(PrimitiveExpr expr) { return parse(expr); }
    public JSONObject visit(IfExpr expr) { return parse(expr); }
    public JSONObject visit(PrintExpr expr) { return parse(expr); }

    /*
     * Constructs a JSON object representation of the
     * given ASTNode. The JSON object looks like:
     * {
     *   "node_type": "",
     *   "children": [ ... ],
     *   "token": ""
     * }
     * The children key's value is a JSON array of arbitrary depth
     * and the token key's value can be "null" or a JSON object 
     * representation of the node's token object.
     * 
     * @param node An ASTNode from which to construct a JSON object.
     * @return The JSON object representation of the given node.
     */
    private JSONObject parse(ASTNode node)
    {
        final JSONObject jo = new JSONObject();
        final JSONArray ja = new JSONArray();

        jo.put("token", node.token() == null 
                ? "null" 
                : parse(node.token()));

        if (node.children().size() != 0)
        {
            for (ASTNode child : node.children())
                ja.put(child.accept(this));
            jo.put("children", ja);
        }

        // Node-specific properties here
        if (node instanceof Expr)
            jo.put("evalType", ((Expr) node).evalType())
                .put("node_type", node.getClass().getSimpleName());
        if (node instanceof LetExpr)
        {
            final LetExpr letExpr = (LetExpr) node;
            jo.put("environment", letExpr.environment() == null
                ? "null"
                : parse(letExpr.environment()));
        }

        return jo;
    }

    /*
     * Constructs a JSON object representation of the
     * given Token. The JSON object looks like:
     * {
     *   "rawText": "",
     *   "type": "",
     *   "startPos": { ... },
     *   "endPos": { ... },
     *   "beginIndex": "",
     *   "beginLineIndex": "",
     *   "endIndex": ""
     * }
     * The value of keys startPos and endPos is "null" or a JSON 
     * object representation of a Position.
     * @param tok A Token from which to construct a JSON object.
     * @return The JSON object representation of the given Token.
     */
    private JSONObject parse(Token tok)
    {
        final JSONObject jo = new JSONObject()
            .put("rawText", tok.rawText())
            .put("type", tok.type())
            .put("startPos", tok.startPos() != null 
                ? parse(tok.startPos()) 
                : "null")
            .put("endPos", tok.startPos() != null 
                ? parse(tok.endPos()) 
                : "null")
            .put("beginIndex", tok.beginIndex())
            .put("beginLineIndex", tok.beginLineIndex())
            .put("endIndex", tok.endIndex());

        return jo;
    }

    /*
     * Constructs a JSON object representation of the
     * given Position. The JSON object looks like:
     * {
     *   "line": "",
     *   "col": ""
     * }
     * @param pos A Position from which to construct a JSON object.
     * @return The JSON object representation of the given Position.
     */
    private JSONObject parse(Position pos)
    {
        final JSONObject jo = new JSONObject()
            .put("line", pos.line())
            .put("col", pos.col());

        return jo;
    }

    private JSONObject parse(Env env)
    {
        final JSONObject jo = new JSONObject();
        final JSONObject table = new JSONObject();

        env.symtab().forEach((id, sym) -> 
            table.put(id, parse(sym)));
        
        jo.put("node_type", env.getClass().getSimpleName())
            .put("parent", env.parent() == null 
                ? "null" 
                : parse(env.parent()))
            .put("table", table);

        return jo;
    }

    private JSONObject parse(Symbol sym)
    {
        final JSONObject jo = new JSONObject();

        jo.put("node_type", sym.getClass().getSimpleName())
            .put("name", sym.name())
            .put("type", sym.type())
            .put("category", sym.category())
            .put("expr", parse(sym.expr()));
        
        return jo;
    }
}
