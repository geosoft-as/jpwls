package no.geosoft.jpwls;

import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

public final class Properties
{
  private final Set<Property> properties_ = new HashSet<>();

  public Properties()
  {
  }

  public void add(Property property)
  {
    if (property == null)
      throw new IllegalArgumentException("property cannot be null");

    properties_.add(property);
  }

  public Set<Property> getAll()
  {
    return Collections.unmodifiableSet(properties_);
  }

  public Property findByName(String name)
  {
    for (Property property : properties_) {
      if (property.getName().equals(name))
        return property;
    }

    // Not found
    return null;
  }

  public Property findByGuid(String guid)
  {
    for (Property property : properties_)
      if (property.getGuid().equals(guid))
        return property;

    // Not found
    return null;
  }

  /** {@inheritDoc} */
  @Override
  public String toString()
  {
    StringBuilder s = new StringBuilder();
    for (Property property : properties_)
      s.append(property.toString() + "\n");

    return s.toString();
  }
}
