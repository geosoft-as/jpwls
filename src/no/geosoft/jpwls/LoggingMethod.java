package no.geosoft.jpwls;

/**
 * Model a PWLS <em>logging method</em>.
 *
 * @author <a href="mailto:jacob.dreyer@geosoft.no">Jacob Dreyer</a>
 */
public final class LoggingMethod
{
  /** Loggging method name. Non-null. */
  private final String name_;

  /** Loggging method description. Non-null. */
  private final String description_;

  /**
   * Create a new logging method.
   *
   * @param name         Loggging method name. Non-null.
   * @param description  Loggging method description.
   * @throws IllegalArgumentException  If name or description is null.
   */
  public LoggingMethod(String name, String description)
  {
    if (name == null)
      throw new IllegalArgumentException("name cannot be null");

    if (description == null)
      throw new IllegalArgumentException("description cannot be null");

    name_ = name;
    description_ = description;
  }

  /**
   * Return name of this logging method.
   *
   * @return  Name of this logging method. Never null.
   */
  public String getName()
  {
    return name_;
  }

  /**
   * Return description of this logging method.
   *
   * @return  Description of this logging method. Never null.
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
