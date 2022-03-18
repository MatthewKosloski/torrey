package me.mtk.torrey.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.mtk.torrey.backend.triple.TargetTriple;

public class TargetRegistry
{
  private Map<String, TargetTriple> registry;

  public TargetRegistry()
  {
    registry = new HashMap<>();
  }

  public boolean hasTarget(String target)
  {
    return registry.containsKey(target);
  }

  public TargetRegistry add(TargetTriple triple)
  {
    registry.put(triple.toString(), triple);
    return this;
  }

  public TargetTriple get(String target)
  {
    return registry.get(target);
  }

  public List<String> getKeys()
  {
    final List<String> keys = new ArrayList<>();
    registry.forEach((k, v) -> keys.add(k));
    return keys;
  }

}
