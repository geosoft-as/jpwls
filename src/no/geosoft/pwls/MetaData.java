package no.geosoft.pwls;

import java.util.Date;

/**
 * Meta-data for an entry in the PWL standard.
 *
 * @author <a href="mailto:jacob.dreyer@geosoft.no">Jacob Dreyer</a>
 */
public final class MetaData
{
  private final Type type_;

  private final Date time_;

  private final String source_;

  private final String email_;

  public enum Type
  {
    CREATED("Created"),
    MODIFIED("Modified"),
    DELETED("Deleted");

    private final String name_;

    private Type(String name)
    {
      name_ = name;
    }

    public String getName()
    {
      return name_;
    }

    public static Type get(String name)
    {
      for (Type type : Type.values()) {
        if (type.getName().equals(name))
          return type;
      }

      // Not found
      return null;
    }
  }

  public MetaData(Type type, Date time, String source, String email)
  {
    type_ = type;
    time_ = new Date(time.getTime());
    source_ = source;
    email_ = email;
  }

  public Type getType()
  {
    return type_;
  }

  public Date getTime()
  {
    return new Date(time_.getTime());
  }

  public String getSource()
  {
    return source_;
  }

  public String getEmail()
  {
    return email_;
  }

  @Override
  public String toString()
  {
    return type_.getName() + " @ " + time_ + " by " + source_ + " (" + email_ + ")";
  }
}
