package no.geosoft.jpwls;

import java.util.Collections;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

public final class Tools
{
  /** All the tools managed by this instance. */
  private final Set<Tool> tools_ = new HashSet<>();

  /** Cache for fast lookup. */
  private final Map<String,Tool> toolsByCode_ = new HashMap<>();

  public Tools()
  {
  }

  public void add(Tool tool)
  {
    if (tool == null)
      throw new IllegalArgumentException("tool cannot be null");

    tools_.add(tool);

    String key = tool.getCode() + tool.getCompanyCode();
    toolsByCode_.put(key, tool);
  }

  public Tool get(String code, int companyCode)
  {
    if (code == null)
      throw new IllegalArgumentException("code cannot be null");

    String key = code + companyCode;
    return toolsByCode_.get(key);
  }

  public Set<Tool> getAll()
  {
    return Collections.unmodifiableSet(tools_);
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
