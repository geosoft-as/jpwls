package no.geosoft.jpwls;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import no.geosoft.jpwls.json.JsonReader;

/**
 * The PWLS model.
 *
 * @author <a href="mailto:jacob.dreyer@geosoft.no">Jacob Dreyer</a>
 */
public final class Pwls
{
  /** The logger instance. */
  private final Logger logger_ = Logger.getLogger(Pwls.class.getName());

  /** PWLS properties. */
  private final Properties properties_;

  /** PWLS companies. */
  private final Companies companies_;

  /** PWLS logging methods. */
  private final LoggingMethods loggingMethods_;

  /** PWLS tool classes. */
  private final ToolClasses toolClasses_;

  /** PWLS tools. */
  private final Tools tools_;

  /** PWLS curves. */
  private final Curves curves_;

  /**
   * Initialize the PWLS model from the specified base URL, typically
   * the GitHub location of the JSON files that makes up the standard.
   *
   * @param baseUrl  Base URL to initialize from. Non-null.
   * @throws IllegalArgumentException  If baseUrl is null.
   */
  public Pwls(String baseUrl)
  {
    if (baseUrl == null)
      throw new IllegalArgumentException("baseUrl cannot be null");

    String url = null;

    //
    // Properties
    //
    Properties properties = new Properties();
    try {
      url = baseUrl + "/properties.json";
      properties = readProperties(url);
    }
    catch (IOException exception) {
      logger_.log(Level.WARNING, "Unable to read properties. Continue without: " + url, exception);
    }
    properties_ = properties;

    //
    // Companies
    //
    Companies companies = new Companies();
    try {
      url = baseUrl + "/companies.json";
      companies = readCompanies(url);
    }
    catch (IOException exception) {
      logger_.log(Level.WARNING, "Unable to read companies. Continue without: " + url, exception);
    }
    companies_ = companies;

    //
    // Logging methods
    //
    LoggingMethods loggingMethods = new LoggingMethods();
    try {
      url = baseUrl + "/loggingMethods.json";
      loggingMethods = readLoggingMethods(url);
    }
    catch (IOException exception) {
      logger_.log(Level.WARNING, "Unable to read logging methods. Continue without: " + url, exception);
    }
    loggingMethods_ = loggingMethods;

    //
    // Tool classes
    //
    ToolClasses toolClasses = new ToolClasses();
    try {
      url = baseUrl + "/toolClasses.json";
      toolClasses = readToolClasses(url);
    }
    catch (IOException exception) {
      logger_.log(Level.WARNING, "Unable to read tool classes. Continue without: " + url, exception);
    }
    toolClasses_ = toolClasses;

    //
    // Tools
    //
    Tools tools = new Tools();
    try {
      url = baseUrl + "/tools.json";
      tools = readTools(url);
    }
    catch (IOException exception) {
      logger_.log(Level.WARNING, "Unable to read tools. Continue without: " + url, exception);
    }
    tools_ = tools;

    //
    // Curves
    //
    Curves curves = new Curves();
    try {
      url = baseUrl + "/curves.json";
      curves = readCurves(url);
    }
    catch (IOException exception) {
      logger_.log(Level.WARNING, "Unable to read curves. Continue without: " + url, exception);
    }
    curves_ = curves;

    //
    // Curves by tool
    //
    try {
      url = baseUrl + "/curvesByTool.json";
      readCurvesByTool(url, tools_, curves_);
    }
    catch (IOException exception) {
      logger_.log(Level.WARNING, "Unable to read curves by tool. Continue without: " + url, exception);
    }
  }

  /**
   * Initialize the PWLS model from the specified folder, typically
   * the local root folder of the JSON files that makes up the standard.
   * <p>
   * Note: This is an alternative constructor suitable for use in
   * unit testing.
   *
   * @param folder  Folder of JSON files to initialize from. Non-null.
   * @throws IllegalArgumentException  If folder is null.
   */
  public Pwls(File folder)
  {
    if (folder == null)
      throw new IllegalArgumentException("folder cannot be null");

    File file = null;

    //
    // Properties
    //
    Properties properties = new Properties();
    try {
      file = new File(folder, "properties.json");
      properties = readProperties(file);
    }
    catch (IOException exception) {
      logger_.log(Level.WARNING, "Unable to read properties. Continue without: " + file, exception);
    }
    properties_ = properties;

    //
    // Companies
    //
    Companies companies = new Companies();
    try {
      file = new File(folder, "companies.json");
      companies= readCompanies(file);
    }
    catch (IOException exception) {
      logger_.log(Level.WARNING, "Unable to read companies. Continue without: " + file, exception);
    }
    companies_ = companies;

    //
    // Logging methods
    //
    LoggingMethods loggingMethods = new LoggingMethods();
    try {
      file = new File(folder, "loggingMethods.json");
      loggingMethods = readLoggingMethods(file);
    }
    catch (IOException exception) {
      logger_.log(Level.WARNING, "Unable to read logging methods. Continue without: " + file, exception);
    }
    loggingMethods_ = loggingMethods;

    //
    // Tool classes
    //
    ToolClasses toolClasses = new ToolClasses();
    try {
      file = new File(folder, "toolClasses.json");
      toolClasses = readToolClasses(file);
    }
    catch (IOException exception) {
      logger_.log(Level.WARNING, "Unable to read tool classes. Continue without: " + file, exception);
    }
    toolClasses_ = toolClasses;

    //
    // Tools
    //
    Tools tools = new Tools();
    try {
      file = new File(folder, "tools.json");
      tools = readTools(file);
    }
    catch (IOException exception) {
      logger_.log(Level.WARNING, "Unable to read tools. Continue without: " + file, exception);
    }
    tools_ = tools;

    //
    // Curves
    //
    Curves curves = new Curves();
    try {
      file = new File(folder, "curves.json");
      curves = readCurves(file);
    }
    catch (IOException exception) {
      logger_.log(Level.WARNING, "Unable to read curves. Continue without: " + file, exception);
    }
    curves_ = curves;

    //
    // Curves by tool
    //
    try {
      file = new File(folder, "curvesByTool.json");
      readCurvesByTool(file, tools_, curves_);
    }
    catch (IOException exception) {
      logger_.log(Level.WARNING, "Unable to read curves by tool. Continue without: " + file, exception);
    }
  }

  /**
   * Get properties of the PWLS model.
   *
   * @param name  Name of property to filter on, or null to get all.
   * @return      Requested properties. Never null.
   */
  public Set<Property> getProperties(String name)
  {
    Set<Property> properties = new HashSet<>();

    for (Property property : properties_.getAll()) {
      if (name != null && !property.getName().equals(name))
        continue;

      properties.add(property);
    }

    return properties;
  }

  /**
   * Get companies of the PWLS model.
   *
   * @param companyCode  Company code to filter on, or null to get all.
   * @return             Requested companies. Never null.
   */
  public Set<Company> getCompanies(Integer companyCode)
  {
    Set<Company> companies = new HashSet<>();

    for (Company company : companies_.getAll()) {
      if (companyCode != null && companyCode != company.getCode())
        continue;

      companies.add(company);
    }

    return companies;
  }

  /**
   * Get logging methods of the PWLS model.
   *
   * @param name  Name of logging method to filetr on, or null to get all.
   * @return      Requested logging methods. Never null.
   */
  public Set<LoggingMethod> getLoggingMethods(String name)
  {
    Set<LoggingMethod> loggingMethods = new HashSet<>();

    for (LoggingMethod loggingMethod : loggingMethods_.getAll()) {
      if (name != null && !loggingMethod.getName().equals(name))
        continue;

      loggingMethods.add(loggingMethod);
    }

    return loggingMethods;
  }

  /**
   * Get tool classes of the PWLS model.
   *
   * @param name  Name of tool class to filter on, or null to get all.
   * @return      Requested tool classes. Never null.
   */
  public Set<ToolClass> getToolClasses(String name)
  {
    Set<ToolClass> toolClasses = new HashSet<>();

    for (ToolClass toolClass : toolClasses_.getAll()) {
      if (name != null && !toolClass.getName().equals(name))
        continue;

      toolClasses.add(toolClass);
    }

    return toolClasses;
  }

  /**
   * Get tools of the PWL model.
   *
   * @param code           Code of tool to filter on, or null to get all.
   * @param companyCode    Company code to filter on, or null to get all.
   * @param group          Group to filter on, or null to get all.
   * @param genericType    Generic type to filter on, or null to get all.
   * @param loggingMethod  Logging method to filter on, or null to get all.
   * @return               Requested tools. Never null.
   */
  public Set<Tool> getTools(String code, Integer companyCode, String group, String genericType, String loggingMethod)
  {
    Set<Tool> tools = new HashSet<>();

    for (Tool tool : tools_.getAll()) {
      if (code != null && !code.equals(tool.getCode()))
        continue;

      if (companyCode != null && !companyCode.equals(tool.getCompanyCode()))
        continue;

      if (group != null && !group.equals(tool.getGroup()))
        continue;

      if (genericType != null && !genericType.equals(tool.getGenericType()))
        continue;

      if (loggingMethod != null && !loggingMethod.equals(tool.getLoggingMethod()))
        continue;

      tools.add(tool);
    }

    return tools;
  }

  /**
   * Get curves of the PWLS model.
   *
   * @param mnemonic     Mnemonic to filter on, or null to get all.
   * @param companyCode  Company code to filter on, or null to get all.
   * @return             Requested curves. Never null.
   */
  public Set<Curve> getCurves(String mnemonic, Integer companyCode)
  {
    Set<Curve> curves = new HashSet<>();

    for (Curve curve : curves_.getAll()) {
      if (mnemonic != null && !mnemonic.equals(curve.getMnemonic()))
        continue;

      if (companyCode != null && !companyCode.equals(curve.getCompanyCode()))
        continue;

      curves.add(curve);
    }

    return curves;
  }

  /**
   * Read properties from the specified URL.
   *
   * @param url  URL to read properties from. Non-null.
   * @return     Properties read. Never null.
   * @throws IOException  If the read operation fails for some reason.
   */
  private Properties readProperties(String url)
    throws IOException
  {
    assert url != null : "url cannot be null";

    logger_.log(Level.INFO, "Read properties from " + url);
    InputStream stream = null;
    try {
      stream = new URL(url).openStream();
      return JsonReader.readProperties(stream);
    }
    catch (MalformedURLException exception) {
      throw new IOException("Invalid URL:" + url, exception);
    }
    finally {
      stream.close();
    }
  }

  /**
   * Read properties from the specified file.
   *
   * @param file  File to read properties from. Non-null.
   * @return      Properties read. Never null.
   * @throws IOException  If the read operation fails for some reason.
   */
  private Properties readProperties(File file)
    throws IOException
  {
    assert file != null : "file cannot be null";

    logger_.log(Level.INFO, "Read properties from " + file);
    InputStream stream = null;
    try {
      stream = new FileInputStream(file);
      return JsonReader.readProperties(stream);
    }
    catch (FileNotFoundException exception) {
      throw new IOException("Invalid file: " + file, exception);
    }
    finally {
      stream.close();
    }
  }

  /**
   * Read companies from the specified URL.
   *
   * @param url  URL to read companies from. Non-null.
   * @return     Companies read. Never null.
   * @throws IOException  If the read operation fails for some reason.
   */
  private Companies readCompanies(String url)
    throws IOException
  {
    assert url != null : "url cannot be null";

    logger_.log(Level.INFO, "Read companies from " + url);
    InputStream stream = null;
    try {
      stream = new URL(url).openStream();
      return JsonReader.readCompanies(stream);
    }
    catch (MalformedURLException exception) {
      throw new IOException("Invalid URL: " + url, exception);
    }
    finally {
      stream.close();
    }
  }

  /**
   * Read companies from the specified file.
   *
   * @param file  File to read companies from. Non-null.
   * @return      Companies read. Never null.
   * @throws IOException  If the read operation fails for some reason.
   */
  private Companies readCompanies(File file)
    throws IOException
  {
    assert file != null : "file cannot be null";

    logger_.log(Level.INFO, "Read companies from " + file);
    InputStream stream = null;
    try {
      stream = new FileInputStream(file);
      return JsonReader.readCompanies(stream);
    }
    catch (FileNotFoundException exception) {
      throw new IOException("Invalid file: " + file, exception);
    }
    finally {
      stream.close();
    }
  }

  /**
   * Read logging methods from the specified URL.
   *
   * @param url  URL to read logging methods from. Non-null.
   * @return     Logging methods read. Never null.
   * @throws IOException  If the read operation fails for some reason.
   */
  private LoggingMethods readLoggingMethods(String url)
    throws IOException
  {
    assert url != null : "url cannot be null";

    logger_.log(Level.INFO, "Read logging methods from " + url);
    InputStream stream = null;
    try {
      stream = new URL(url).openStream();
      return JsonReader.readLoggingMethods(stream);
    }
    catch (MalformedURLException exception) {
      throw new IOException("Invalid URL: " + url, exception);
    }
    finally {
      stream.close();
    }
  }

  /**
   * Read logging methods from the specified file.
   *
   * @param file  File to read logging methods from. Non-null.
   * @return      Logging methods read. Never null.
   * @throws IOException  If the read operation fails for some reason.
   */
  private LoggingMethods readLoggingMethods(File file)
    throws IOException
  {
    assert file != null : "file cannot be null";

    logger_.log(Level.INFO, "Read logging methods from " + file);
    InputStream stream = null;
    try {
      stream = new FileInputStream(file);
      return JsonReader.readLoggingMethods(stream);
    }
    catch (FileNotFoundException exception) {
      throw new IOException("Invalid file: " + file, exception);
    }
    finally {
      stream.close();
    }
  }

  /**
   * Read tool classes from the specified URL.
   *
   * @param url  URL to read tool classes from. Non-null.
   * @return     Tool classes read. Never null.
   * @throws IOException  If the read operation fails for some reason.
   */
  private ToolClasses readToolClasses(String url)
    throws IOException
  {
    assert url != null : "url cannot be null";

    logger_.log(Level.INFO, "Read tool classes from " + url);
    InputStream stream = null;
    try {
      stream = new URL(url).openStream();
      return JsonReader.readToolClasses(stream);
    }
    catch (MalformedURLException exception) {
      throw new IOException("Invalid URL: " + url, exception);
    }
    finally {
      stream.close();
    }
  }

  /**
   * Read tool classes from the specified file.
   *
   * @param file  File to read tool classes from. Non-null.
   * @return      Tool classes read. Never null.
   * @throws IOException  If the read operation fails for some reason.
   */
  private ToolClasses readToolClasses(File file)
    throws IOException
  {
    assert file != null : "file cannot be null";

    logger_.log(Level.INFO, "Read tool classes from " + file);
    InputStream stream = null;
    try {
      stream = new FileInputStream(file);
      return JsonReader.readToolClasses(stream);
    }
    catch (FileNotFoundException exception) {
      throw new IOException("Invalid file: " + file, exception);
    }
    finally {
      stream.close();
    }
  }

  /**
   * Read tools from the specified URL.
   *
   * @param url  URL to read tools from. Non-null.
   * @return     Tools read. Never null.
   * @throws IOException  If the read operation fails for some reason.
   */
  private Tools readTools(String url)
    throws IOException
  {
    assert url != null : "url cannot be null";

    logger_.log(Level.INFO, "Read tools from " + url);
    InputStream stream = null;
    try {
      stream = new URL(url).openStream();
      return JsonReader.readTools(stream);
    }
    catch (MalformedURLException exception) {
      throw new IOException("Invalid URL: " + url, exception);
    }
    finally {
      stream.close();
    }
  }

  /**
   * Read tools from the specified file.
   *
   * @param file  File to read tools from. Non-null.
   * @return      Tools read. Never null.
   * @throws IOException  If the read operation fails for some reason.
   */
  private Tools readTools(File file)
    throws IOException
  {
    assert file != null : "file cannot be null";

    logger_.log(Level.INFO, "Read tools from " + file);
    InputStream stream = null;
    try {
      stream = new FileInputStream(file);
      return JsonReader.readTools(stream);
    }
    catch (FileNotFoundException exception) {
      throw new IOException("Invalid file: " + file, exception);
    }
    finally {
      stream.close();
    }
  }

  /**
   * Read curves from the specified URL.
   *
   * @param url  URL to read curves from. Non-null.
   * @return     Curves read. Never null.
   * @throws IOException  If the read operation fails for some reason.
   */
  private Curves readCurves(String url)
    throws IOException
  {
    assert url != null : "url cannot be null";

    logger_.log(Level.INFO, "Read curves from " + url);
    InputStream stream = null;
    try {
      stream = new URL(url).openStream();
      return JsonReader.readCurves(stream);
    }
    catch (MalformedURLException exception) {
      throw new IOException("Invalid URL: " + url, exception);
    }
    finally {
      stream.close();
    }
  }

  /**
   * Read curves from the specified file.
   *
   * @param file  File to read curves from. Non-null.
   * @return      Curves read. Never null.
   * @throws IOException  If the read operation fails for some reason.
   */
  private Curves readCurves(File file)
    throws IOException
  {
    assert file != null : "file cannot be null";

    logger_.log(Level.INFO, "Read curves from " + file);
    InputStream stream = null;
    try {
      stream = new FileInputStream(file);
      return JsonReader.readCurves(stream);
    }
    catch (FileNotFoundException exception) {
      throw new IOException("Invalid file: " + file, exception);
    }
    finally {
      stream.close();
    }
  }

  /**
   * Read curves by tool from the specified URL.
   *
   * @param url     URL to read curves by tool from. Non-null.
   * @param tools   Tools instance to populate. Non-null.
   * @param curves  Curves instance to populate from. Non-null.
   */
  private void readCurvesByTool(String url, Tools tools, Curves curves)
    throws IOException
  {
    assert url != null : "url cannot be null";
    assert tools != null : "tools cannot be null";
    assert curves != null : "curves cannot be null";

    logger_.log(Level.INFO, "Read curves by tool from " + url);
    InputStream stream = null;
    try {
      stream = new URL(url).openStream();
      JsonReader.readCurvesByTool(stream, tools, curves);
    }
    catch (MalformedURLException exception) {
      throw new IOException("Invalid URL: " + url, exception);
    }
    finally {
      stream.close();
    }
  }

  /**
   * Read curves by tool from the specified file.
   *
   * @param url     URL to read curves by tool from. Non-null.
   * @param tools   Tools instance to populate. Non-null.
   * @param curves  Curves instance to populate from. Non-null.
   */
  private void readCurvesByTool(File file, Tools tools, Curves curves)
    throws IOException
  {
    assert file != null : "file cannot be null";
    assert tools != null : "tools cannot be null";
    assert curves != null : "curves cannot be null";

    logger_.log(Level.INFO, "Read curves by tool from " + file);
    InputStream stream = null;
    try {
      stream = new FileInputStream(file);
      JsonReader.readCurvesByTool(stream, tools, curves);
    }
    catch (FileNotFoundException exception) {
      throw new IOException("Invalid file: " + file, exception);
    }
    finally {
      stream.close();
    }
  }
}
