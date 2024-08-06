package no.geosoft.jpwls;

import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

/**
 * Model all the companies defined by the PWLS standard.
 *
 * @author <a href="mailto:jacob.dreyer@geosoft.no">Jacob Dreyer</a>
 */
public final class Companies
{
  /** All companies defined by the PWLS standard. */
  private final Set<Company> companies_ = new HashSet<>();

  /**
   * Create an empty companies instance.
   */
  public Companies()
  {
    // Nothing
  }

  /**
   * Add the specified company to this instance.
   *
   * @param company  Company to add. Non-null.
   * @throws IllegalArgumentException  If company is null.
   */
  public void add(Company company)
  {
    if (company == null)
      throw new IllegalArgumentException("company cannot be null");

    companies_.add(company);
  }

  /**
   * Return all the companies of this instance.
   *
   * @return  All the companies of this instance. Never null.
   */
  public Set<Company> getAll()
  {
    return Collections.unmodifiableSet(companies_);
  }

  /**
   * Find company of the specified company code.
   *
   * @param companyCode  Company code of company to get.
   * @return             The requested company, or null if not found,
   */
  public Company find(int companyCode)
  {
    for (Company company : companies_) {
      if (company.getCompanyCode() == companyCode)
        return company;
    }

    // Not found
    return null;
  }

  /** {@inheritDoc} */
  @Override
  public String toString()
  {
    StringBuilder s = new StringBuilder();
    for (Company company : companies_)
      s.append(company.toString() + "\n");

    return s.toString();
  }
}
