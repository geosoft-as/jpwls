package no.geosoft.jpwls;

/**
 * Model a PWLS <em>property</em> i.e. <em>what</em> is being measured
 * by a specific curve.
 *
 * @author <a href="mailto:jacob.dreyer@geosoft.no">Jacob Dreyer</a>
 */
public final class Property
{
  /** Property name. Non-null. */
  private final String name_;

  /** Property description. Non-null. */
  private final String description_;

  /** The quantity of this property. Non-null. */
  private final String quantity_;

  /** Property unique ID. Non-null. */
  private final String guid_;

  /** Property sort order. */
  private final int sortOrder_;

  /** Abstract specifier. */
  private final boolean isAbstract_;

  /** Parent property. */
  private Property parent_;

  /**
   * Create a new property instance.
   *
   * @param name         Property name. Non-null.
   * @param description  Property description. Non-null.
   * @param quantity     The quantity of this property. Non-null.
   * @param guid         Property unique ID. Non-null.
   * @param sortOrder    Property sort order.
   * @param isAbstract   Abstract specifier.
   * @throws IllegalArgumentException  If name, description, quentity or guid is null.
   */
  public Property(String name,
                  String description,
                  String quantity,
                  String guid,
                  int sortOrder,
                  boolean isAbstract)
  {
    if (name == null)
      throw new IllegalArgumentException("name cannot be null");

    if (description == null)
      throw new IllegalArgumentException("description cannot be null");

    if (quantity == null)
      throw new IllegalArgumentException("quantity cannot be null");

    if (guid == null)
      throw new IllegalArgumentException("guid cannot be null");

    name_ = name;
    description_ = description;
    quantity_ = quantity;
    guid_ = guid;
    sortOrder_ = sortOrder;
    isAbstract_ = isAbstract;
  }

  /**
   * Return name of this property.
   *
   * @return  Name of this property. Never null.
   */
  public String getName()
  {
    return name_;
  }

  /**
   * Return property description.
   *
   * @return  Property description. Never null.
   */
  public String getDescription()
  {
    return description_;
  }

  /**
   * Return quantity of this property.
   *
   * @return  Quantity of this property. Never null.
   */
  public String getQuantity()
  {
    return quantity_;
  }

  /**
   * Return the unique ID of this property.
   *
   * @return  Unique ID of this property. Never null.
   */
  public String getGuid()
  {
    return guid_;
  }

  /**
   * Set parent property of this property.
   *
   * @param parent  Parent property. Non-null.
   * @throws IllegalArgumentException  If parent is null.
   */
  public void setParent(Property parent)
  {
    if (parent == null)
      throw new IllegalArgumentException("parent cannot be null");

    parent_ = parent;
  }

  /**
   * Return the parent property of this property.
   *
   * @return  Parent property of this property. Null if at root level.
   */
  public Property getParent()
  {
    return parent_;
  }

  /**
   * Return sort order of this property.
   *
   * @return  Sort order of this property,
   */
  public int getSortOrder()
  {
    return sortOrder_;
  }

  /**
   * Return if this property is abstract or not.
   *
   * @return  True if this property is abstract, false otherwise.
   */
  public boolean isAbstract()
  {
    return isAbstract_;
  }

  /** {@inheritDoc} */
  @Override
  public String toString()
  {
    // Find which ancestry level we are at
    String parents = "";
    Property parent = parent_;
    while (parent != null) {
      parents = parents.isEmpty() ? parent.getName() : parents + " - " + parent.getName();
      parent = parent.getParent();
    }

    StringBuilder s = new StringBuilder();
    s.append("Name........: " + name_ + "\n");
    s.append("Description.: " + description_ + "\n");
    s.append("Quantity....: " + quantity_ + "\n");
    s.append("GUID........: " + guid_ + "\n");
    s.append("Parent......: " + parents + "\n");
    s.append("Sort order..: " + sortOrder_ + "\n");
    s.append("isAbstract..: " + isAbstract_ + "\n");
    return s.toString();
  }
}
