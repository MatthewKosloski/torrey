package me.mtk.torrey.ir;

import java.util.Map;
import java.util.HashMap;

/**
 * Represents a binary operator.
 */
public final class BinaryOperator extends Operator
{
    // Maps an enum prop to its op text
    private static final Map<BinaryOpType, String> store;

    static
    {
        store = new HashMap<>();
        store.put(BinaryOpType.ADD, "+");
        store.put(BinaryOpType.SUB, "-");
        store.put(BinaryOpType.MULT, "*");
        store.put(BinaryOpType.DIV, "/");
    }

    public BinaryOperator(BinaryOpType op) {
        super(store.get(op));
    }
}