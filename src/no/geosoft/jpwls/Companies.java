package no.geosoft.jpwls;

import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

public final class Companies
{
  private final Set<Company> companies_ = new HashSet<>();

  public Companies()
  {
  }

  public void add(Company company)
  {
    if (company == null)
      throw new IllegalArgumentException("company cannot be null");

    companies_.add(company);
  }

  public Company get(int companyCode)
  {
    for (Company company : companies_) {
      if (company.getCode() == companyCode)
        return company;
    }

    // Not found
    return null;
  }

  public Set<Company> getAll()
  {
    return Collections.unmodifiableSet(companies_);
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
