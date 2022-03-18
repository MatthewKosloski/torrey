package me.mtk.torrey.backend;

/**
 * Handles the construction of compiler backends at runtime.
 */
public final class CompilerBackendFactory
{
  private TargetRegistry registry;

  public CompilerBackendFactory(TargetRegistry registry)
  {
    this.registry = registry;
  }

  public CompilerBackend makeBackendFromTarget(String target)
  {
    if (registry.hasTarget(target))
    {
      return registry.get(target).makeBackend();
    }

    // Maybe replace this with an empty optional??
    return null;
  }
}
