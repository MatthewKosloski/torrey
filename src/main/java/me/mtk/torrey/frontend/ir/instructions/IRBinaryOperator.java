package me.mtk.torrey.frontend.ir.instructions;

import java.util.Map;
import java.util.HashMap;

/**
 * Represents a binary operator.
 */
public final class IRBinaryOperator extends IROperator
{
    // Maps an IR binary operator type to its text (terminal symbol)
    private static final Map<IRBinaryOpType, String> store;

    static
    {
        store = new HashMap<>();
        store.put(IRBinaryOpType.ADD, "+");
        store.put(IRBinaryOpType.SUB, "-");
        store.put(IRBinaryOpType.MULT, "*");
        store.put(IRBinaryOpType.DIV, "/");
        store.put(IRBinaryOpType.EQUAL, "==");
        store.put(IRBinaryOpType.NEQUAL, "!=");
        store.put(IRBinaryOpType.LT, "<");
        store.put(IRBinaryOpType.LTE, "<=");
        store.put(IRBinaryOpType.GT, ">");
        store.put(IRBinaryOpType.GTE, ">=");
    }

    public IRBinaryOperator(IRBinaryOpType op) {
        super(store.get(op));
    }
}