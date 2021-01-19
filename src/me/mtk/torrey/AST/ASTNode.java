package me.mtk.torrey.AST;

import java.util.List;
import java.util.ArrayList;
import me.mtk.torrey.Lexer.*;

/**
 * A homogeneous AST node type that is to be extended
 * to create nodes for a normalized heterogenous AST.
 */
public abstract class ASTNode
{
    // From which token did we create this node?
    private Token token;

    // A normalized list of child nodes (rather than named fields)
    // makes it much easier to build external tree visitors.
    private List<ASTNode> children;

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
    public List<ASTNode> getChildren()
    {
        return children;
    }

    /**
     * A convenience method to retrieve the first child.
     * 
     * @return The first child of this node.
     */
    public ASTNode first()
    {
        return children.get(0);
    }

    /**
     * A convenience method to retrieve the second child.
     * 
     * @return The second child of this node.
     */
    public ASTNode second()
    {
        return children.get(1);
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
