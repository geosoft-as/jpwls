package no.geosoft.jpwls;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Model all the logging methods defined by the PWLS standard.
 *
 * @author <a href="mailto:jacob.dreyer@geosoft.no">Jacob Dreyer</a>
 */
public final class LoggingMethods
{
  /** All logging methods defined by the PWLS standard. */
  private final Set<LoggingMethod> loggingMethods_ = new HashSet<>();

  /**
   * Create an empty logging methods instance.
   */
  public LoggingMethods()
  {
    // Nothing
  }

  /**
   * Add the specified logging method to this instance.
   *
   * @param loggingMethod  Logging method to add. Non-null.
   * @throws IllegalArgumentException  If loggingMethod is null.
   */
  public void add(LoggingMethod loggingMethod)
  {
    if (loggingMethod == null)
      throw new IllegalArgumentException("loggingMethod cannot be null");

    loggingMethods_.add(loggingMethod);
  }

  /**
   * Return all the logging methods of this instance.
   *
   * @return  All the logging methods of this instance. Never null.
   */
  public Set<LoggingMethod> getAll()
  {
    return Collections.unmodifiableSet(loggingMethods_);
  }

  /**
   * Find logging method of the specified name.
   *
   * @param name  Name of logging method to find. Non-null.
   * @return      The requested logging method, or null if not found,
   * @throws IllegalArgumentException  If name is null.
   */
  public LoggingMethod find(String name)
  {
    if (name == null)
      throw new IllegalArgumentException("name cannot be null");

    for (LoggingMethod loggingMethod : loggingMethods_)
      if (loggingMethod.getName().equals(name))
        return loggingMethod;

    // Not found
    return null;
  }

  /** {@inheritDoc} */
  @Override
  public String toString()
  {
    StringBuilder s = new StringBuilder();
    for (LoggingMethod loggingMethod : loggingMethods_)
      s.append(loggingMethod.toString() + "\n");

    return s.toString();
  }
}
