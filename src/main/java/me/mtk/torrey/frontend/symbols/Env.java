package me.mtk.torrey.frontend.symbols;

import java.util.Map;
import java.util.HashMap;

/**
 * Implements an environment, that is, a chain of symbol 
 * tables, where each table maps names to symbols. Each 
 * environment in the chain is a scope.
 */
public class Env 
{
    // The symbols of this environment.
    private Map<String, Symbol> table;

    // The enclosing environment.
    private Env parent;

    public Env(Env parent)
    {
        this.parent = parent;
        table = new HashMap<>();
    }

    /**
     * Binds the identifier to the symbol in
     * this environment's symbol table.
     * 
     * @param id An identifier.
     * @param sym A symbol to be bounded to the identifier.
     */
    public void put(String id, Symbol sym)
    {
        table.put(id, sym);
    }

    /**
     * Starts from the current environment and traverses
     * up the entire lexical scope chain, looking for
     * the symbol bounded to the given identifier. If no
     * symbol is found in any of the symbol tables in
     * the lexical scope chain, then null will be returned.
     * 
     * @param id An identifier.
     * @return The symbol bounded to the given identifier or 
     * null if no such symbol exists.
     */
    public Symbol get(String id)
    {
        for (Env e = this; e != null; e = e.parent)
        {
            // Starting from this scope, look for the symbol
            // bounded to the given identifier. If it's not found
            // in this scope, check the parent scope.
            Symbol found = e.table.get(id);
            if (found != null) return found;
        }

        // From this scope, we traversed all the way up the
        // symbol table chain and have not found any symbol
        // to which id is bound.
        return null;
    }

    /**
     * Indicates whether the specified identifier
     * has already been declared in this scope.
     * 
     * @param id An identifier.
     * @return True if the specified identifier
     * has already been declared in this scope;
     * False otherwise.
     */
    public boolean has(String id)
    {
        return table.get(id) != null;
    }

    /**
     * Returns the parent environment.
     * 
     * @return The parent environment.
     */
    public Env parent()
    {
        return parent;
    }
    
    /**
     * Returns this scope's symbol table.
     * @return The scope's symbol table.
     */
    public Map<String, Symbol> symtab()
    {
        return table;
    }

}
