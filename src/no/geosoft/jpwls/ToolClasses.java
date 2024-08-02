package no.geosoft.jpwls;

import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

public final class ToolClasses
{
  private final Set<ToolClass> toolClasses_ = new HashSet<>();

  public ToolClasses()
  {
  }

  public void add(ToolClass toolClass)
  {
    if (toolClass == null)
      throw new IllegalArgumentException("toolClass cannot be null");

    toolClasses_.add(toolClass);
  }

  public Set<ToolClass> getAll()
  {
    return Collections.unmodifiableSet(toolClasses_);
  }

  public ToolClass get(String name)
  {
    if (name == null)
      throw new IllegalArgumentException("name cannot be null");

    for (ToolClass toolClass : toolClasses_)
      if (toolClass.getName().equals(name))
        return toolClass;

    // Not found
    return null;
  }

  /** {@inheritDoc} */
  @Override
  public String toString()
  {
    StringBuilder s = new StringBuilder();
    for (ToolClass toolClass : toolClasses_)
      s.append(toolClass.toString() + "\n");

    return s.toString();
  }
}
