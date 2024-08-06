package no.geosoft.jpwls;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Model all the tools defined by the PWLS standard.
 *
 * @author <a href="mailto:jacob.dreyer@geosoft.no">Jacob Dreyer</a>
 */
public final class Tools
{
  /** All the tools managed by this instance. */
  private final Set<Tool> tools_ = new HashSet<>();

  /** Tools by toolCode/company code. Cached for performance. */
  private final Map<String,Tool> toolsByCode_ = new HashMap<>();

  /**
   * Create an empty curves instance.
   */
  public Tools()
  {
    // Nothing
  }

  /**
   * Add the specified tool to this instance.
   *
   * @param tool  Tool to add. Non-null.
   * @throws IllegalArgumentException  If tool is null.
   */
  public void add(Tool tool)
  {
    if (tool == null)
      throw new IllegalArgumentException("tool cannot be null");

    tools_.add(tool);

    String key = tool.getToolCode() + tool.getCompanyCode();
    toolsByCode_.put(key, tool);
  }

  /**
   * Return all the tools of this instance.
   *
   * @return  All the tools of this instance. Never null.
   */
  public Set<Tool> getAll()
  {
    return Collections.unmodifiableSet(tools_);
  }

  /**
   * Find the tool of the specified tool code/company code.
   *
   * @param toolCode     Tool code of tool to find. Non.null.
   * @param companyCode  Company code of tool to find.
   * @return             Requested tool, or null if not found.
   * @throws IllegalArgumentException  If toolCode is null.
   */
  public Tool find(String toolCode, int companyCode)
  {
    if (toolCode == null)
      throw new IllegalArgumentException("toolCode cannot be null");

    String key = toolCode + companyCode;
    return toolsByCode_.get(key);
  }

  /** {@inheritDoc} */
  @Override
  public String toString()
  {
    StringBuilder s = new StringBuilder();
    for (Tool tool : tools_)
      s.append(tool.toString() + "\n");
    return s.toString();
  }
}
