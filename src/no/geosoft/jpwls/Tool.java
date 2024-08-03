package no.geosoft.jpwls;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Model a PWLS <em>tool</em>.
 *
 * @author <a href="mailto:jacob.dreyer@geosoft.no">Jacob Dreyer</a>
 */
public final class Tool
{
  /** Company code of this tool. */
  private final Integer companyCode_;

  /** Tool code. */
  private final String code_;

  /** Tool group. */
  private final String group_;

  /** Tool marketing name. */
  private final String marketingName_;

  /** Tool description. */
  private final String description_;

  /** The generic type of this tool. */
  private final String genericType_;

  /** The logging methods of this tool. */
  private final String loggingMethod_;

  /** Description of the type of this tool. */
  private final String typeDescription_;

  /** The curves associated with this tool. */
  private final Set<Curve> curves_ = new HashSet<>();

  /**
   * Create a new tool.
   *
   * @param code             Tool code. Non-null.
   * @param companyCode      Code of company of tool. Non-null.
   * @param group            Tool group. Non-null.
   * @param marketingName    Tool marketing name.
   * @param description      Tool description.
   * @param genericType      Generic type of this tool.
   * @param loggingMethod    Logging method of this tool.
   * @param typeDescription  Description of the type of this tool.
   */
  public Tool(String code,
              Integer companyCode,
              String group,
              String marketingName,
              String description,
              String genericType,
              String loggingMethod,
              String typeDescription)
  {
    code_ = code;
    companyCode_ = companyCode;
    group_ = group;
    marketingName_ = marketingName;
    description_ = description;
    genericType_ = genericType;
    loggingMethod_ = loggingMethod;
    typeDescription_ = typeDescription;
  }

  public void addCurve(Curve curve)
  {
    if (curve == null)
      throw new IllegalArgumentException("curve cannot be null");

    curves_.add(curve);
  }

  public String getCode()
  {
    return code_;
  }

  public Integer getCompanyCode()
  {
    return companyCode_;
  }

  public String getGroup()
  {
    return code_;
  }

  public String getMarketingName()
  {
    return marketingName_;
  }

  public String getDescription()
  {
    return description_;
  }

  public String getGenericType()
  {
    return genericType_;
  }

  public String getLoggingMethod()
  {
    return loggingMethod_;
  }

  public String getTypeDescription()
  {
    return typeDescription_;
  }

  public Set<Curve> getCurves()
  {
    return Collections.unmodifiableSet(curves_);
  }

  /** {@inheritDoc} */
  @Override
  public String toString()
  {
    StringBuilder s = new StringBuilder();
    s.append("Code.............: " + code_ + "\n");
    s.append("Company code.....: " + companyCode_ + "\n");
    s.append("Group............: " + group_ + "\n");
    s.append("Marketing name...: " + marketingName_ + "\n");
    s.append("Description......: " + description_ + "\n");
    s.append("Generic type.....: " + genericType_ + "\n");
    s.append("Logging method...: " + loggingMethod_ + "\n");
    s.append("Type description.: " + typeDescription_ + "\n");
    s.append("Curves...........: ");
    for (Curve curve : curves_)
      s.append(curve.getMnemonic() + " ");
    s.append("\n");

    return s.toString();
  }
}
