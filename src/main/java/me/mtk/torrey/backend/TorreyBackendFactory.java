package me.mtk.torrey.backend;

import java.util.Map;
import java.util.HashMap;

import me.mtk.torrey.backend.targets.x86_64.pc.linux.X8664PCLinuxTarget;
import me.mtk.torrey.backend.triple.*;


/**
 * Handles the construction of compiler backends at runtime.
 */
public final class TorreyBackendFactory
{
    // Maps target triple strings to target triple instances.
    private static Map<String, TargetTriple> targetStringToTripleMap;

    static
    {
        targetStringToTripleMap = new HashMap<>();

        // This is where the compiler backends are installed.
        
        final TargetTriple x8664PCLinuxTarget = new X8664PCLinuxTarget();
        targetStringToTripleMap.put(x8664PCLinuxTarget.toString(),
            x8664PCLinuxTarget);
    }

    public static TorreyBackend makeBackendFromTarget(String target)
    {
        if (targetStringToTripleMap.containsKey(target))
        {
            return targetStringToTripleMap.get(target)
                .makeBackend();
        }

        // Maybe replace this with an empty optional??
        return null;
    }

    public static Map<String, TargetTriple> targetStringToTripleMap()
    {
        return targetStringToTripleMap;
    }
}
