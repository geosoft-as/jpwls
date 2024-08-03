package no.geosoft.jpwls.json;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.ParseException;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

import no.geosoft.jpwls.Companies;
import no.geosoft.jpwls.Company;
import no.geosoft.jpwls.Curve;
import no.geosoft.jpwls.Curves;
import no.geosoft.jpwls.LoggingMethod;
import no.geosoft.jpwls.LoggingMethods;
import no.geosoft.jpwls.Properties;
import no.geosoft.jpwls.Property;
import no.geosoft.jpwls.Tool;
import no.geosoft.jpwls.ToolClass;
import no.geosoft.jpwls.ToolClasses;
import no.geosoft.jpwls.Tools;

/**
 * JSON reader for the PWLS model.
 *
 * @author <a href="mailto:jacob.dreyer@geosoft.no">Jacob Dreyer</a>
 */
public final class JsonReader
{
  /** The logger instance. */
  private static final Logger logger_ = Logger.getLogger(JsonReader.class.getName());

  /**
   * Private constructor to prevent client instantiation.
   */
  private JsonReader()
  {
    assert false : "This constructor should never be called";
  }

  /**
   * Read PWLS properties from the specified JSON stream.
   *
   * @param stream  JSON stream to read from. Non-null.
   * @return        The properties read. Never null.
   * @throws IllegalArgumentException  If stream is null.
   */
  public static Properties readProperties(InputStream stream)
  {
    if (stream == null)
      throw new IllegalArgumentException("stream inputannot be null");

    Properties properties = new Properties();

    javax.json.JsonReader reader = Json.createReader(stream);
    JsonArray jsonArray = reader.readArray();

    // Temporary holder of parents
    Map<Property,String> parents = new HashMap<>();

    for (int i = 0; i < jsonArray.size(); i++) {
      JsonObject jsonObject = jsonArray.getJsonObject(i);

      String name = jsonObject.getString("name", null);
      String description = jsonObject.getString("description", null);
      String quantity = jsonObject.getString("quantity", null);
      String guid = jsonObject.getString("guid", null);
      String parent = jsonObject.getString("parent", null);
      int sortOrder = jsonObject.getInt("sortOrder", -1);
      boolean isAbstract = jsonObject.getBoolean("isAbstract", false);

      Property property = new Property(name,
                                       description,
                                       quantity,
                                       guid,
                                       sortOrder,
                                       isAbstract);

      parents.put(property, parent);

      properties.add(property);
    }

    // Resolve parents
    for (Property property : properties.getAll()) {
      String parentName = parents.get(property);
      Property parentProperty = properties.findByName(parentName);
      if (parentProperty == null)
        logger_.log(Level.WARNING, "Missing parent property for " + property.getName());

      // Keep parent == null if at root level
      if (parentProperty != property)
        property.setParent(parentProperty);
    }

    return properties;
  }

  /**
   * Read PWLS properties from the specified JSON file.
   *
   * @param file JSON file to read from. Non-null.
   * @return     The properties read. Never null.
   * @throws IllegalArgumentException  If file is null.
   * @throws IOException  If the read operation fails for some reason.
   */
  public static Properties readProperties(File file)
    throws IOException
  {
    if (file == null)
      throw new IllegalArgumentException("file cannot be null");

    FileInputStream inputStream = null;
    try {
      inputStream = new FileInputStream(file);
      return readProperties(inputStream);
    }
    finally {
      if (inputStream != null)
        inputStream.close();
    }
  }

  /**
   * Read PWLS companies from the specified JSON stream.
   *
   * @param stream  JSON stream to read from. Non-null.
   * @return        The companies read. Never null.
   * @throws IllegalArgumentException  If stream is null.
   * @throws IOException  If the read operation fails for some reason.
   */
  public static Companies readCompanies(InputStream stream)
    throws IOException
  {
    if (stream == null)
      throw new IllegalArgumentException("stream cannot be null");

    Companies companies = new Companies();

    javax.json.JsonReader reader = Json.createReader(stream);
    JsonArray jsonArray = reader.readArray();

    for (int i = 0; i < jsonArray.size(); i++) {
      JsonObject jsonObject = jsonArray.getJsonObject(i);

      int code = jsonObject.getInt("code");
      String name = jsonObject.getString("name");
      Company company = new Company(code, name);

      companies.add(company);
    }

    return companies;
  }

  /**
   * Read PWLS companies from the specified JSON file.
   *
   * @param file  JSON file to read from. Non-null.
   * @return      The companies read. Never null.
   * @throws IllegalArgumentException  If file is null.
   * @throws IOException               If the read operation fails for some reason.
   */
  public static Companies readCompanies(File file)
    throws IOException
  {
    if (file == null)
      throw new IllegalArgumentException("file cannot be null");

    FileInputStream inputStream = null;
    try {
      inputStream = new FileInputStream(file);
      return readCompanies(inputStream);
    }
    finally {
      if (inputStream != null)
        inputStream.close();
    }
  }

  /**
   * Read PWLS curves from the specified JSON stream.
   *
   * @param stream  JSON stream to read from. Non-null.
   * @return        The curves read. Never null.
   * @throws IllegalArgumentException  If stream is null.
   */
  public static Curves readCurves(InputStream stream)
  {
    if (stream == null)
      throw new IllegalArgumentException("stream cannot be null");

    Curves curves = new Curves();

    javax.json.JsonReader reader = Json.createReader(stream);
    JsonArray jsonArray = reader.readArray();

    for (int i = 0; i < jsonArray.size(); i++) {
      JsonObject jsonObject = jsonArray.getJsonObject(i);

      String mnemonic = jsonObject.getString("mnemonic", null);
      String shortMnemonic = jsonObject.getString("shortMnemonic", null);
      int companyCode = jsonObject.getInt("companyCode", -1);
      String property = jsonObject.getString("property", null);
      String quantity = jsonObject.getString("quantity", null);
      String description = jsonObject.getString("description", null);

      Curve curve = new Curve(mnemonic,
                              shortMnemonic,
                              companyCode != -1 ? companyCode : null,
                              property,
                              quantity,
                              description);

      curves.add(curve);
    }

    return curves;
  }

  /**
   * Read PWLS curves from the specified JSON file.
   *
   * @param file  JSON file to read from. Non-null.
   * @return      The curves read. Never null.
   * @throws IllegalArgumentException  If file is null.
   * @throws IOException  If the read operation fails for some reason.
   */
  public static Curves readCurves(File file)
    throws IOException
  {
    if (file == null)
      throw new IllegalArgumentException("file cannot be null");

    FileInputStream inputStream = null;
    try {
      inputStream = new FileInputStream(file);
      return readCurves(inputStream);
    }
    finally {
      if (inputStream != null)
        inputStream.close();
    }
  }

  /**
   * Read PWLS tools from the specified JSON stream.
   *
   * @param stream  JSON stream to read from. Non-null.
   * @return        The tools read. Never null.
   * @throws IllegalArgumentException  If stream is null.
   */
  public static Tools readTools(InputStream stream)
  {
    if (stream == null)
      throw new IllegalArgumentException("stream cannot be null");

    Tools tools = new Tools();

    javax.json.JsonReader reader = Json.createReader(stream);
    JsonArray jsonArray = reader.readArray();

    for (int i = 0; i < jsonArray.size(); i++) {
      JsonObject jsonObject = jsonArray.getJsonObject(i);

      String code = jsonObject.getString("code", null);
      int companyCode = jsonObject.getInt("companyCode", -1);
      String group = jsonObject.getString("group", null);
      String marketingName = jsonObject.getString("marketingName", null);
      String description = jsonObject.getString("description", null);
      String genericType = jsonObject.getString("genericType", null);
      String loggingMethod = jsonObject.getString("loggingMethod", null);
      String typeDescription = jsonObject.getString("typeDescription", null);

      Tool tool = new Tool(code,
                           companyCode != -1 ? companyCode : null,
                           group,
                           marketingName,
                           description,
                           genericType,
                           loggingMethod,
                           typeDescription);

      tools.add(tool);
    }

    return tools;
  }

  /**
   * Read PWLS tools from the specified JSON file.
   *
   * @param file  JSON file to read from. Non-null.
   * @return      The tools read. Never null.
   * @throws IllegalArgumentException  If file is null.
   * @throws IOException  If the read operation fails for some reason.
   */
  public static Tools readTools(File file)
    throws IOException
  {
    if (file == null)
      throw new IllegalArgumentException("file cannot be null");

    FileInputStream inputStream = null;
    try {
      inputStream = new FileInputStream(file);
      return readTools(inputStream);
    }
    finally {
      if (inputStream != null)
        inputStream.close();
    }
  }

  /**
   * Read PWLS tool classes from the specified JSON stream.
   *
   * @param stream  JSON stream to read from. Non-null.
   * @return        The tool classes read. Never null.
   * @throws IllegalArgumentException  If stream is null.
   */
  public static ToolClasses readToolClasses(InputStream stream)
  {
    if (stream == null)
      throw new IllegalArgumentException("stream cannot be null");

    ToolClasses toolClasses = new ToolClasses();

    javax.json.JsonReader reader = Json.createReader(stream);
    JsonArray jsonArray = reader.readArray();

    for (int i = 0; i < jsonArray.size(); i++) {
      JsonObject jsonObject = jsonArray.getJsonObject(i);

      String name = jsonObject.getString("name", null);
      String description = jsonObject.getString("description", null);

      ToolClass toolClass = new ToolClass(name, description);

      toolClasses.add(toolClass);
    }

    return toolClasses;
  }

  /**
   * Read PWLS tool classes from the specified JSON file.
   *
   * @param file  JSON file to read from. Non-null.
   * @return      The tools read. Never null.
   * @throws IllegalArgumentException  If file is null.
   * @throws IOException  If the read operation fails for some reason.
   */
  public static ToolClasses readToolClasses(File file)
    throws IOException
  {
    if (file == null)
      throw new IllegalArgumentException("file cannot be null");

    FileInputStream inputStream = null;
    try {
      inputStream = new FileInputStream(file);
      return readToolClasses(inputStream);
    }
    finally {
      if (inputStream != null)
        inputStream.close();
    }
  }

  /**
   * Read PWLS logging methods from the specified JSON stream.
   *
   * @param stream  JSON stream to read from. Non-null.
   * @return        The logging methods read. Never null.
   * @throws IllegalArgumentException  If stream is null.
   */
  public static LoggingMethods readLoggingMethods(InputStream stream)
  {
    if (stream == null)
      throw new IllegalArgumentException("stream cannot be null");

    LoggingMethods loggingMethods = new LoggingMethods();

    javax.json.JsonReader reader = Json.createReader(stream);
    JsonArray jsonArray = reader.readArray();

    for (int i = 0; i < jsonArray.size(); i++) {
      JsonObject jsonObject = jsonArray.getJsonObject(i);

      String name = jsonObject.getString("name", null);
      String description = jsonObject.getString("description", null);

      LoggingMethod loggingMethod = new LoggingMethod(name, description);

      loggingMethods.add(loggingMethod);
    }

    return loggingMethods;
  }

  /**
   * Read PWLS logging methods from the specified JSON file.
   *
   * @param file  JSON file to read from. Non-null.
   * @return      The logging methods read. Never null.
   * @throws IllegalArgumentException  If file is null.
   * @throws IOException  If the read operation fails for some reason.
   */
  public static LoggingMethods readLoggingMethods(File file)
    throws IOException
  {
    if (file == null)
      throw new IllegalArgumentException("file cannot be null");

    FileInputStream inputStream = null;
    try {
      inputStream = new FileInputStream(file);
      return readLoggingMethods(inputStream);
    }
    finally {
      if (inputStream != null)
        inputStream.close();
    }
  }

  /**
   * Read PWLS curves by tool from the specified JSON stream.
   *
   * @param stream  JSON stream to read from. Non-null.
   * @param tools   The tools instance to populate. Non-null
   * @param curves  The curves instance to populate from. Non-null.
   * @throws IllegalArgumentException  If streamm tools or curves is null.
   */
  public static void readCurvesByTool(InputStream stream, Tools tools, Curves curves)
  {
    if (stream == null)
      throw new IllegalArgumentException("stream cannot be null");

    if (tools == null)
      throw new IllegalArgumentException("tools cannot be null");

    if (curves == null)
      throw new IllegalArgumentException("curves cannot be null");

    javax.json.JsonReader reader = Json.createReader(stream);
    JsonArray jsonArray = reader.readArray();

    for (int i = 0; i < jsonArray.size(); i++) {
      JsonObject jsonObject = jsonArray.getJsonObject(i);

      String toolCode = jsonObject.getString("toolCode", null);
      int companyCode = jsonObject.getInt("companyCode", -1);
      String curveMnemonic = jsonObject.getString("curveMnemonic", null);

      Tool tool = tools.get(toolCode, companyCode);
      if (tool == null)
        logger_.log(Level.WARNING, "Unknown tool: " + toolCode + " for company=" + companyCode);

      Curve curve = curves.get(curveMnemonic, companyCode);
      if (curve == null)
        logger_.log(Level.WARNING, "Unknown curve: " + curveMnemonic + " for company=" + companyCode);

      if (tool != null && curve != null)
        tool.addCurve(curve);
    }
  }

  /**
   * Read PWLS curves by tool from the specified JSON file.
   *
   * @param file    JSON file to read from. Non-null.
   * @param tools   The tools instance to populate. Non-null
   * @param curves  The curves instance to populate from. Non-null.
   * @throws IllegalArgumentException  If streamm tools or curves is null.
   * @throws IOException  If the read operation fails for some reason.
   */
  public static void readCurvesByTool(File file, Tools tools, Curves curves)
    throws IOException
  {
    if (file == null)
      throw new IllegalArgumentException("file cannot be null");

    if (tools == null)
      throw new IllegalArgumentException("tools cannot be null");

    if (curves == null)
      throw new IllegalArgumentException("curves cannot be null");

    FileInputStream inputStream = null;
    try {
      inputStream = new FileInputStream(file);
      readCurvesByTool(inputStream, tools, curves);
    }
    finally {
      if (inputStream != null)
        inputStream.close();
    }
  }
}


