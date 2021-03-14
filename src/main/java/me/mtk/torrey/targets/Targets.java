package me.mtk.torrey.targets;

import java.util.HashMap;
import java.util.Map;

public final class Targets 
{
    public static Map<String, TargetTriple> registry;

    static
    {
        registry = new HashMap<>();
        registry.put("x86_64-pc-linux", new TargetTriple(TargetArch.X86_64, 
            TargetVendor.PC, TargetSys.LINUX));
    }
}
