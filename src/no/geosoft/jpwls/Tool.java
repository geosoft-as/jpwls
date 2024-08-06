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
  /** Tool code, i.e. its name. Non-null. */
  private final String toolCode_;

  /** Company code of this tool. */
  private final int companyCode_;

  /** Tool group. Null if none or N/A. */
  private final String group_;

  /** Tool marketing name. Null if none. */
  private final String marketingName_;

  /** Tool description. Null if none provided. */
  private final String description_;

  /** The generic type of this tool. Null if N/A. */
  private final String genericType_;

  /** The logging methods of this tool. Null if N/A. */
  private final String loggingMethod_;

  /** Description of the type of this tool. Null if N/A. */
  private final String typeDescription_;

  /** The curves associated with this tool. */
  private final Set<Curve> curves_ = new HashSet<>();

  /**
   * Create a new tool instance.
   *
   * @param toolCode         Tool code. Non-null.
   * @param companyCode      Code of company of tool.
   * @param group            Tool group. Null if none or N/A.
   * @param marketingName    Tool marketing name. Null if none.
   * @param description      Tool description. Null if none provided.
   * @param genericType      Generic type of this tool. Null if N/A.
   * @param loggingMethod    Logging method of this tool. Null if N/A.
   * @param typeDescription  Description of the type of this tool. Null if N/A.
   * @throws IllegalArgumentException  If toolCode is null.
   */
  public Tool(String toolCode,
              int companyCode,
              String group,
              String marketingName,
              String description,
              String genericType,
              String loggingMethod,
              String typeDescription)
  {
    if (toolCode == null)
      throw new IllegalArgumentException("toolCode cannot be null");

    toolCode_ = toolCode;
    companyCode_ = companyCode;
    group_ = group;
    marketingName_ = marketingName;
    description_ = description;
    genericType_ = genericType;
    loggingMethod_ = loggingMethod;
    typeDescription_ = typeDescription;
  }

  /**
   * Associate the specified curve with this tool.
   *
   * @param curve  Curve to add. Non-null.
   * @throws IllegalArgumentException  If curve is null.
   */
  public void addCurve(Curve curve)
  {
    if (curve == null)
      throw new IllegalArgumentException("curve cannot be null");

    curves_.add(curve);
  }

  /**
   * Return the tool code of this tool.
   *
   * @return  The tool code of this tool. Never null.
   */
  public String getToolCode()
  {
    return toolCode_;
  }

  /**
   * Return the company code of the company of this tool.
   *
   * @return  The company code of the company of this tool. Never null.
   */
  public int getCompanyCode()
  {
    return companyCode_;
  }

  /**
   * Return the group of this tool.
   *
   * @return  The group of this tool. Null if none or N/A.
   */
  public String getGroup()
  {
    return group_;
  }

  /**
   * Return the marketing name of this tool.
   *
   * @return  Marketing name of this tool. Null if none.
   */
  public String getMarketingName()
  {
    return marketingName_;
  }

  /**
   * Return the description of this tool.
   *
   * @return  Description of this tool. Null if none provided.
   */
  public String getDescription()
  {
    return description_;
  }

  /**
   * Return the generic tool type of this tool.
   *
   * @return  Generic tool type of this tool. Null if N/A.
   */
  public String getGenericType()
  {
    return genericType_;
  }

  /**
   * Return the logging method of this tool.
   *
   * @return  Logging method of this tool. Null if N/A.
   */
  public String getLoggingMethod()
  {
    return loggingMethod_;
  }

  /**
   * Return the tool type description of this tool.
   *
   * @return  Tool type description of this tool. Null if N/A.
   */
  public String getTypeDescription()
  {
    return typeDescription_;
  }

  /**
   * Return the curves associated with this tool.
   *
   * @return  The curves associated with this tool. Never null.
   */
  public Set<Curve> getCurves()
  {
    return Collections.unmodifiableSet(curves_);
  }

  /** {@inheritDoc} */
  @Override
  public String toString()
  {
    StringBuilder s = new StringBuilder();
    s.append("Tool code........: " + toolCode_ + "\n");
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
