package no.geosoft.pwls;

public final class LoggingMethod
{
  private final String name_;

  private final String description_;

  public LoggingMethod(String name, String description)
  {
    if (name == null)
      throw new IllegalArgumentException("name cannot be null");

    if (description == null)
      throw new IllegalArgumentException("description cannot be null");

    name_ = name;
    description_ = description;
  }

  public String getName()
  {
    return name_;
  }

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
