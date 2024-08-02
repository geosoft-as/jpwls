package no.geosoft.jpwls;

import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

public final class LoggingMethods
{
  private final Set<LoggingMethod> loggingMethods_ = new HashSet<>();

  public LoggingMethods()
  {
  }

  public void add(LoggingMethod loggingMethod)
  {
    if (loggingMethod == null)
      throw new IllegalArgumentException("loggingMethod cannot be null");

    loggingMethods_.add(loggingMethod);
  }

  public LoggingMethod get(String name)
  {
    if (name == null)
      throw new IllegalArgumentException("name cannot be null");

    for (LoggingMethod loggingMethod : loggingMethods_)
      if (loggingMethod.getName().equals(name))
        return loggingMethod;

    // Not found
    return null;
  }

  public Set<LoggingMethod> getAll()
  {
    return Collections.unmodifiableSet(loggingMethods_);
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
