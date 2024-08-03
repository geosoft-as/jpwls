package no.geosoft.jpwls;

/**
 * Model a PWLS <em>tool class</em>.
 *
 * @author <a href="mailto:jacob.dreyer@geosoft.no">Jacob Dreyer</a>
 */
public final class ToolClass
{
  /** Tool class name. Non-null. */
  private final String name_;

  /** Tool class description. Non-null. */
  private final String description_;

  /**
   * Create a new tool class.
   *
   * @param name         Tool class name. Non-null.
   * @param description  Tool class description.
   * @throws IllegalArgumentException  If name or description is null.
   */
  public ToolClass(String name, String description)
  {
    if (name == null)
      throw new IllegalArgumentException("name cannot be null");

    if (description == null)
      throw new IllegalArgumentException("description cannot be null");

    name_ = name;
    description_ = description;
  }

  /**
   * Return name of this tool class.
   *
   * @return  Name of this tool class. Never null.
   */
  public String getName()
  {
    return name_;
  }

  /**
   * Return description of this tool class.
   *
   * @return  Description of this tool class. Never null.
   */
  public String getDescription()
  {
    return description_;
  }

  /** {@inheritDoc} */
  @Override
  public String toString()
  {
    return name_ + " " + description_;
  }
}
