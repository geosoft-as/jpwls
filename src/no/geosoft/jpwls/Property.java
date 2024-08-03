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

  public Property(String name,
                  String description,
                  String quantity,
                  String guid,
                  int sortOrder,
                  boolean isAbstract)
  {
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

  public String getQuantity()
  {
    return quantity_;
  }

  public String getGuid()
  {
    return guid_;
  }

  public void setParent(Property parent)
  {
    parent_ = parent;
  }

  public Property getParent()
  {
    return parent_;
  }

  public int getSortOrder()
  {
    return sortOrder_;
  }

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
