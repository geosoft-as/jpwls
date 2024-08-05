package no.geosoft.jpwls;

/**
 * Model a PWLS company.
 *
 * @author <a href="mailto:jacob.dreyer@geosoft.no">Jacob Dreyer</a>
 */
public final class Company
{
  /** Company code. */
  private final int companyCode_;

  /** Comany name. Non-null. */
  private final String name_;

  /**
   * Create a new PWLS company.
   *
   * @param companyCode  Company code.
   * @param name         Company name. Non-null.
   * @throws IllegalArgumentException  If name is null.
   */
  public Company(int companyCode, String name)
  {
    if (name == null)
      throw new IllegalArgumentException("name cannot be null");

    companyCode_ = companyCode;
    name_ = name;
  }

  /**
   * Return the company code of this company.
   *
   * @return  The company code of this company.
   */
  public int getCompanyCode()
  {
    return companyCode_;
  }

  /**
   * Return the name of this company.
   *
   * @return  The name of this company. Never null.
   */
  public String getName()
  {
    return name_;
  }

  /** {@inheritDoc} */
  @Override
  public String toString()
  {
    return name_ + " (" + companyCode_ + ")";
  }
}
