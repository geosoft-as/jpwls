package no.geosoft.pwls;

public final class Company
{
  private final int code_;

  private final String name_;

  public Company(int code, String name)
  {
    if (name == null)
      throw new IllegalArgumentException("name cannot be null");

    code_ = code;
    name_ = name;
  }

  public int getCode()
  {
    return code_;
  }

  public String getName()
  {
    return name_;
  }

  /** {@inheritDoc} */
  @Override
  public String toString()
  {
    return name_ + " (" + code_ + ")";
  }
}
