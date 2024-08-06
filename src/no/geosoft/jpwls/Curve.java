package no.geosoft.jpwls;

/**
 * Model a PWLS <em>curve</em>.
 *
 * @author <a href="mailto:jacob.dreyer@geosoft.no">Jacob Dreyer</a>
 */
public final class Curve
{
  /** Curve mnemonic, i.e. its name. Non-null. */
  private final String mnemonic_;

  /** Curve short mnemonic. Null if N/A. */
  private final String lisMnemonic_;

  /** Company code of this curve. Null if N/A. */
  private final Integer companyCode_;

  /** Property of this curve, i.e. what is measured. Non-null. */
  private final String property_;

  /** Quantity of the property. Non-null. */
  private final String quantity_;

  /** Curve description. Null if none provided. */
  private final String description_;

  /**
   * Create a new curve instance.
   *
   * @param mnemonic     Curve mnemonic. Non-null.
   * @param lisMnemonic  Curve short mnemonic. Null if N/A.
   * @param companyCode  Company code of this curve. Null if N/A.
   * @param property     Property of this curve. Non-null.
   * @param quantity     Quantity of the property. Non-null.
   * @param description  Curve description. Null if none provided.
   * @throws IllegalArgumentException  If mnemonic, property or quantity is null.
   */
  public Curve(String mnemonic,
               String lisMnemonic,
               Integer companyCode,
               String property,
               String quantity,
               String description)

  {
    if (mnemonic == null)
      throw new IllegalArgumentException("mnemonic cannot be null");

    if (property == null)
      throw new IllegalArgumentException("property cannot be null");

    if (quantity == null)
      throw new IllegalArgumentException("quantity cannot be null");

    mnemonic_ = mnemonic;
    lisMnemonic_ = lisMnemonic;
    companyCode_ = companyCode;
    property_ = property;
    quantity_ = quantity;
    description_ = description;
  }

  /**
   * Return the mnemonic of this curve, i.e. its name.
   *
   * @return  The mnemonic of this curve. Never null.
   */
  public String getMnemonic()
  {
    return mnemonic_;
  }

  /**
   * Return the optional short (LIS) mnemonic of this curve.
   *
   * @return  The short mnemonic of this curve. Null if N/A.
   */
  public String getLisMnemonic()
  {
    return lisMnemonic_;
  }

  /**
   * Return the company code of the company of this curve.
   *
   * @return  The company code of the company of this curve. Null if N/A.
   */
  public Integer getCompanyCode()
  {
    return companyCode_;
  }

  /**
   * Return the property of this curve.
   *
   * @return  The property of this curve. Never null.
   */
  public String getProperty()
  {
    return property_;
  }

  /**
   * Return the quantity of this curve.
   *
   * @return  The quantity of this curve. Never null.
   */
  public String getQuantity()
  {
    return quantity_;
  }

  /**
   * Return the description of this curve.
   *
   * @return  The description of this curve. Null if none provided.
   */
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
