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
public final class PrettyPrinterVisitor implements ASTNodeVisitor
{

  private JSONObject nextJSONObject;

  /**
   * Returns a JSON representation of the given AST.
   * @param program The root node of an AST.
   */
  public void visit(Program program)
  {
    parse(program);
  }

  public void visit(LetExpr expr)
  {
    parse(expr);
  }
  public void visit(LetBinding binding)
  {
    parse(binding);
  }
  public void visit(LetBindings bindings)
  {
    parse(bindings);
  }
  public void visit(IdentifierExpr expr)
  {
    parse(expr);
  }
  public void visit(ArithmeticExpr expr)
  {
    parse(expr);
  }
  public void visit(CompareExpr expr)
  {
    parse(expr);
  }
  public void visit(UnaryExpr expr)
  {
    parse(expr);
  }
  public void visit(IntegerExpr expr)
  {
    parse(expr);
  }
  public void visit(BooleanExpr expr)
  {
    parse(expr);
  }
  public void visit(IfExpr expr)
  {
    parse(expr);
  }
  public void visit(IfThenElseExpr expr)
  {
    parse(expr);
  }
  public void visit(PrintExpr expr)
  {
    parse(expr);
  }

  public String getPrint()
  {
    return nextJSONObject.toString(2);
  }

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
  private void parse(ASTNode node)
  {
    final JSONObject jo = new JSONObject();
    final JSONArray ja = new JSONArray();

    if (node.token() == null)
    {
      jo.put("token", "null");
    }
    else
    {
      parse(node.token());
      jo.put("token", nextJSONObject);
    }

    if (node.children().size() != 0)
    {
      for (ASTNode child : node.children())
      {
        child.accept(this);
        ja.put(nextJSONObject);
      }
      jo.put("children", ja);
    }

    // Node-specific properties here
    if (node instanceof Expr)
      jo.put("evalType", ((Expr) node).evalType())
        .put("node_type", node.getClass().getSimpleName());

    if (node instanceof LetExpr)
    {
      final LetExpr letExpr = (LetExpr) node;

      if (letExpr.environment() == null)
      {
        jo.put("environment", "null");
      }
      else
      {
        parse(letExpr.environment());
        jo.put("environment", nextJSONObject);
      }
    }

    nextJSONObject = jo;
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
  private void parse(Token tok)
  {
    final JSONObject jo = new JSONObject()
      .put("rawText", tok.rawText())
      .put("type", tok.type());

      if (tok.startPos() == null)
      {
        jo.put("startPos", "null");
      }
      else
      {
        parse(tok.endPos());
        jo.put("environment", nextJSONObject);
      }

      if (tok.endPos() == null)
      {
        jo.put("endPos", "null");
      }
      else
      {
        parse(tok.endPos());
        jo.put("endPos", nextJSONObject);
      }

      jo.put("beginIndex", tok.beginIndex())
        .put("beginLineIndex", tok.beginLineIndex())
        .put("endIndex", tok.endIndex());

    nextJSONObject = jo;
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
  private void parse(Position pos)
  {
    final JSONObject jo = new JSONObject()
      .put("line", pos.line())
      .put("col", pos.col());

    nextJSONObject = jo;
  }

  private void parse(Env env)
  {
    final JSONObject jo = new JSONObject();
    final JSONObject table = new JSONObject();

    env.symtab().forEach((id, sym) ->
    {
      parse(sym);
      table.put(id, nextJSONObject);
    });

    jo.put("node_type", env.getClass().getSimpleName());

    if (env.parent() == null)
    {
      jo.put("parent", "null");
    }
    else
    {
      parse(env.parent());
      jo.put("parent", nextJSONObject);
    }

    nextJSONObject = jo;
  }

  private void parse(Symbol sym)
  {
    final JSONObject jo = new JSONObject();

    jo.put("node_type", sym.getClass().getSimpleName())
      .put("name", sym.name())
      .put("category", sym.category())
      .put("exprToString", sym.expr());

    nextJSONObject = jo;
  }
}
