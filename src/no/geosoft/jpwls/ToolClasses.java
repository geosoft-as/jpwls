package no.geosoft.jpwls;

import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

/**
 * Model all the tool classes defined by the PWLS standard.
 *
 * @author <a href="mailto:jacob.dreyer@geosoft.no">Jacob Dreyer</a>
 */
public final class ToolClasses
{
  /** All tool classes defined by the PWLS standard. */
  private final Set<ToolClass> toolClasses_ = new HashSet<>();

  /**
   * Create an empty tool classes instance.
   */
  public ToolClasses()
  {
    // Nothing
  }

  /**
   * Add the specified tool class to this instance.
   *
   * @param toolClass  Tool class to add. Non-null.
   * @throws IllegalArgumentException  If toolClass is null.
   */
  public void add(ToolClass toolClass)
  {
    if (toolClass == null)
      throw new IllegalArgumentException("toolClass cannot be null");

    toolClasses_.add(toolClass);
  }

  /**
   * Return all the tool classes of this instance.
   *
   * @return  All the tool classes of this instance. Never null.
   */
  public Set<ToolClass> getAll()
  {
    return Collections.unmodifiableSet(toolClasses_);
  }

  /**
   * Find tool class of the specified name.
   *
   * @param name  Name of tool class to find. Non-null.
   * @return      The requested tool class, or null if not found,
   * @throws IllegalArgumentException  If name is null.
   */
  public ToolClass find(String name)
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
