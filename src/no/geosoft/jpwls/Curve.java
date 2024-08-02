package no.geosoft.jpwls;

public final class Curve
{
  private final String mnemonic_;

  private final String lisMnemonic_;

  private final Integer companyCode_;

  private final String property_;

  private final String quantity_;

  private final String description_;

  public Curve(String mnemonic,
               String lisMnemonic,
               Integer companyCode,
               String property,
               String quantity,
               String description)
  {
    mnemonic_ = mnemonic;
    lisMnemonic_ = lisMnemonic;
    companyCode_ = companyCode;
    property_ = property;
    quantity_ = quantity;
    description_ = description;
  }

  public String getMnemonic()
  {
    return mnemonic_;
  }

  public String getLisMnemonic()
  {
    return lisMnemonic_;
  }

  public Integer getCompanyCode()
  {
    return companyCode_;
  }

  public String getProperty()
  {
    return property_;
  }

  public String getQuantity()
  {
    return quantity_;
  }

  public String getDescription()
  {
    return description_;
  }

  /** {@inheritDoc} */
  @Override
  public String toString()
  {
    StringBuilder s = new StringBuilder();
    s.append("Mnemonic......: " + mnemonic_ + "\n");
    s.append("LIS mnemonic..: " + lisMnemonic_ + "\n");
    s.append("Company code..: " + companyCode_ + "\n");
    s.append("Property......: " + property_ + "\n");
    s.append("Quantity......: " + quantity_ + "\n");
    s.append("Description...: " + description_ + "\n");

    return s.toString();
  }
}
