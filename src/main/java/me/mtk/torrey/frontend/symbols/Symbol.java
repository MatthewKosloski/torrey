package me.mtk.torrey.frontend.symbols;

import me.mtk.torrey.frontend.analysis.DataType;
import me.mtk.torrey.frontend.ast.Expr;
import me.mtk.torrey.frontend.ir.addressing.IRTempAddress;

public class Symbol
{
    private String name;
    private DataType type;
    private SymCategory category;
    private IRTempAddress address;
    private Expr expr;

    public Symbol(String name, DataType type, SymCategory category, Expr expr)
    {
        this.name = name;
        this.type = type;
        this.category = category;
        this.expr = expr;
    }

    public String name()
    {
        return name;
    }

    public DataType type()
    {
        return type;
    }

    public SymCategory category()
    {
        return category;
    }

    public Expr expr()
    {
        return expr;
    }

    public void setAddress(IRTempAddress address)
    {
        this.address = address;
    }

    public IRTempAddress address()
    {
        return address;
    }
}
