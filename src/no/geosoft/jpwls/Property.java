package no.geosoft.jpwls;

public final class Property
{
  private final String name_;

  private final String description_;

  private final String quantity_;

  private final String guid_;

  private final int sortOrder_;

  private final boolean isAbstract_;

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

  public String getName()
  {
    return name_;
  }

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
