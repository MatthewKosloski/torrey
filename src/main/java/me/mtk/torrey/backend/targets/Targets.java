package me.mtk.torrey.backend.targets;

import java.util.HashMap;
import java.util.Map;

import me.mtk.torrey.backend.TorreyBackend;
import me.mtk.torrey.backend.targets.x86_64.pc.linux.X8664PCLinuxBackend;

/**
 * Registers target languages by mapping a target
 * triple string to a backend that compiles down
 * to the target described by the target string.
 */
public final class Targets
{
    public static Map<String, TorreyBackend> registry;

    static
    {
        registry = new HashMap<>();
        
        final TargetTriple x86PCLinux = new TargetTriple(TargetArch.X86_64, 
            TargetVendor.PC, TargetSys.LINUX);

        registry.put(x86PCLinux.toString(),
            new X8664PCLinuxBackend(x86PCLinux));
    }
}
