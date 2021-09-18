package me.mtk.torrey.frontend.ir.instructions;

import java.util.Map;
import java.util.HashMap;

/**
 * Represents an unary operator.
 */
public final class IRUnaryOperator extends IROperator
{
    // Maps an enum prop to its op text
    private static final Map<IRUnaryOpType, String> store;

    static
    {
        store = new HashMap<>();
        store.put(IRUnaryOpType.MINUS, "-");
    }

    public IRUnaryOperator(IRUnaryOpType op) {
        super(store.get(op));
    }
}