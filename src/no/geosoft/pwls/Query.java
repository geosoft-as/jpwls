package no.geosoft.pwls;

import java.util.Map;
import java.util.HashMap;
import java.net.URLDecoder;
import java.io.UnsupportedEncodingException;

/**
 * Generic class for handling REST queries.
 *
 * @author <a href="mailto:jacob.dreyer@geosoft.no">Jacob Dreyer</a>
 */
public final class Query
{
  /** The individual key/value pars of the query. */
  private Map<String,String> entries_ = new HashMap<>();

  public Query(String queryString)
  {
    if (queryString != null)
      entries_.putAll(split(queryString));
  }

  private static Map<String,String> split(String queryString)
  {
    Map<String,String> entries = new HashMap<>();

    String[] pairs = queryString.split("&");
    for (String pair : pairs) {
      int index = pair.indexOf('=');
      try {
        String key = URLDecoder.decode(pair.substring(0, index), "UTF-8");
        String value = URLDecoder.decode(pair.substring(index + 1), "UTF-8");

        entries.put(key, value);
      }
      catch (UnsupportedEncodingException exception) {
        assert false : "This will never happen";
      }
    }

    return entries;
  }

  public String getString(String key, String defaultValue)
  {
    String value = entries_.get(key);
    return value != null ? value : defaultValue;
  }

  public String getString(String key)
  {
    return getString(key, null);
  }

  public Integer getInteger(String key, Integer defaultValue)
  {
    String value = entries_.get(key);

    if (value == null)
      return defaultValue;

    try {
      return Integer.parseInt(value);
    }
    catch (NumberFormatException exception) {
      return defaultValue;
    }
  }

  public Integer getInteger(String key)
  {
    return getInteger(key, null);
  }

  public boolean getBoolean(String key, boolean defaultValue)
  {
    String value = entries_.get(key);
    if (value == null)
      return defaultValue;

    return value.equalsIgnoreCase("true");
  }

  public boolean getBoolean(String key)
  {
    return getBoolean(key, false);
  }
}
