package me.mtk.torrey.frontend.ast;

import java.util.List;
import java.util.ArrayList;
import me.mtk.torrey.frontend.lexer.Token;
import me.mtk.torrey.frontend.lexer.TokenType;

/**
 * A homogeneous abstract syntax tree (AST) node type
 * that is to be extended to create nodes for a normalized
 * heterogenous AST. The token (interior node) stores the
 * operation and the children represent the arguments to
 * the operation.
 */
public abstract class ASTNode
{
  // From which token did we create this node?
  private Token token;

  // The parent of this node.
  private ASTNode parent;

  // A normalized list of child nodes (rather than named fields)
  // makes it much easier to build external tree visitors.
  private List<ASTNode> children;

  /**
   * Calls the appropriate visit() method on the visitor.
   */
  public abstract void accept(ASTNodeVisitor visitor);

  /**
   * Construct a new AST node from a token.
   *
   * @param token A token from which we create an AST node.
   */
  public ASTNode(Token token)
  {
    this.token = token;
    children = new ArrayList<>();
  }

  /**
   * Adds a child to this node's normalized list of children.
   *
   * @param child A child node.
   */
  public void addChild(ASTNode child)
  {
    child.parent = this;
    children.add(child);
  }

  /**
   * Returns the token type of the token associated with this
   * AST node. This is used by external visitors to distinguish
   * between different AST nodes.
   *
   * @return The TokenType of this node's token.
   */
  public TokenType getNodeType()
  {
    return token.type();
  }

  /**
   * Returns a normalized list of the node's children.
   *
   * @return The children of this node.
   */
  public List<ASTNode> children()
  {
    return children;
  }

  /**
   * Returns the parent node.
   *
   * @return The parent node.
   */
  public ASTNode parent()
  {
    return parent;
  }

  /**
   * Returns the expression at the specified
   * position in the child list. If no child
   * expression is at the specified position,
   * then null is returned.
   *
   * @param i An index into the child list.
   * @return The expression at the specified position
   * or null.
   */
  public ASTNode get(int i)
  {
    if (i < children.size())
      return children.get(i);
    else
      return null;
  }

  /**
   * A convenience method to retrieve the first child.
   *
   * @return The first child of this node.
   */
  public ASTNode first()
  {
    return get(0);
  }

  /**
   * A convenience method to retrieve the second child.
   *
   * @return The second child of this node.
   */
  public ASTNode second()
  {
    return get(1);
  }

  /**
   * A convenience method to retrieve the last child.
   *
   * @return The last child of this node.
   */
  public ASTNode last()
  {
    return get(children.size() - 1);
  }

  /**
   * Retuns the token from which this AST node was created.
   *
   * @return A token.
   */
  public Token token()
  {
    return token;
  }

  public String toString()
  {
    final StringBuilder sb = new StringBuilder();

    if (children.size() != 0)
      sb.append("(");

    sb.append(token.rawText());

    for (ASTNode child : children)
      sb.append(" " + child.toString());

    if (children.size() != 0)
      sb.append(")");

    return sb.toString();
  }
}
