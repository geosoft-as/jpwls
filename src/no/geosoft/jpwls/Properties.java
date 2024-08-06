package no.geosoft.jpwls;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Model all the <em>properties</em> defined by the PWLS standard.
 *
 * @author <a href="mailto:jacob.dreyer@geosoft.no">Jacob Dreyer</a>
 */
public final class Properties
{
  /** All properties defined by the PWLS standard. */
  private final Set<Property> properties_ = new HashSet<>();

  /**
   * Create an empty properties instance.
   */
  public Properties()
  {
    // Nothing
  }

  /**
   * Add the specified property to this instance.
   *
   * @param property  Property to add. Non-null.
   * @throws IllegalArgumentException  If property is null.
   */
  public void add(Property property)
  {
    if (property == null)
      throw new IllegalArgumentException("property cannot be null");

    properties_.add(property);
  }

  /**
   * Return all the propertries of this instance.
   *
   * @return  All the properties of this instance. Never null.
   */
  public Set<Property> getAll()
  {
    return Collections.unmodifiableSet(properties_);
  }

  /**
   * Return property of the given name.
   *
   * @param name  Name of property to find. Non-null.
   * @return      The requested property, or null if not found.
   * @throws IllegalArgumentException  If name is null.
   */
  public Property findByName(String name)
  {
    if (name == null)
      throw new IllegalArgumentException("name cannot be null");

    for (Property property : properties_) {
      if (property.getName().equals(name))
        return property;
    }

    // Not found
    return null;
  }

  /**
   * Return property of the given GUID.
   *
   * @param guid  GUID of property to find. Non-null.
   * @return      The requested property, or null if not found.
   * @throws IllegalArgumentException  If guid is null.
   */
  public Property findByGuid(String guid)
  {
    if (guid == null)
      throw new IllegalArgumentException("guid cannot be null");

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
