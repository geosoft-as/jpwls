package no.geosoft.pwls.json;

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

import no.geosoft.pwls.Companies;
import no.geosoft.pwls.Company;
import no.geosoft.pwls.Curve;
import no.geosoft.pwls.Curves;
import no.geosoft.pwls.ISO8601DateParser;
import no.geosoft.pwls.LoggingMethod;
import no.geosoft.pwls.LoggingMethods;
import no.geosoft.pwls.Properties;
import no.geosoft.pwls.Property;
import no.geosoft.pwls.Tool;
import no.geosoft.pwls.ToolClass;
import no.geosoft.pwls.ToolClasses;
import no.geosoft.pwls.Tools;

public final class JsonReader
{
  /** The logger instance. */
  private static final Logger logger_ = Logger.getLogger(JsonReader.class.getName());

  private JsonReader()
  {
    assert false : "This constructor should never be called";
  }

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

  public static Companies readCompanies(File file)
    throws IOException
  {
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
   * Read PWLS properties from the specigied JSON stream.
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

  public static Properties readProperties(File file)
    throws IOException
  {
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

  public static Curves readCurves(File file)
    throws IOException
  {
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

  public static Tools readTools(File file)
    throws IOException
  {
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

  public static ToolClasses readToolClasses(File file)
    throws IOException
  {
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

  public static LoggingMethods readLoggingMethods(File file)
    throws IOException
  {
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

  public static void readCurvesByTool(InputStream stream, Tools tools, Curves curves)
  {
    if (stream == null)
      throw new IllegalArgumentException("stream cannot be null");

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

  public static void readCurvesByTool(File file, Tools tools, Curves curves)
    throws IOException
  {
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


  public static void main(String[] arguments)
  {
    try {
      String BASE_URL = "https://raw.githubusercontent.com/rabbagast/pwls/main";

      File logsFile = new File("C:/Users/jacob/dev/Petroware/PWLS/PWLS v3.0 Logs.xlsx");
      File propertiesFile = new File("C:/Users/jacob/dev/Petroware/PWLS/PWLS v3.0 Properties.xlsx");

      /* TOOLS
      Tools tools = no.geosoft.jpwls.excel.ExcelReader.readTools(logsFile);

      System.out.println("Complete reading Excel: " + tools.getAll().size());

      javax.json.JsonArrayBuilder toolsBuilder = JsonWriter.get(tools);
      File file = new File("C:/Users/jacob/dev/Petroware/PWLS/tools.json");
      java.io.FileOutputStream outputStream = new java.io.FileOutputStream(file);
      JsonWriter.save(outputStream, toolsBuilder.build());
      outputStream.close();

      System.out.println("Complete writing JSON: " + file);

      Tools tools2 = JsonReader.readTools(file);
      System.out.println(tools2);
      */

      /* TOOL CLASSES
      ToolClasses toolClasses = no.geosoft.jpwls.excel.ExcelReader.readToolClasses(logsFile);

      System.out.println("Complete reading Excel: " + toolClasses.getAll().size());

      javax.json.JsonArrayBuilder toolClassesBuilder = JsonWriter.get(toolClasses);
      File file = new File("C:/Users/jacob/dev/Petroware/PWLS/toolClasses.json");
      java.io.FileOutputStream outputStream = new java.io.FileOutputStream(file);
      JsonWriter.save(outputStream, toolClassesBuilder.build());
      outputStream.close();

      System.out.println("Complete writing JSON: " + file);

      ToolClasses toolClasses2 = JsonReader.readToolClasses(file);
      System.out.println(toolClasses2);
      */

      /* LOGGING METHODS
      LoggingMethods loggingMethods = no.geosoft.jpwls.excel.ExcelReader.readLoggingMethods(logsFile);

      System.out.println("Complete reading Excel: " + loggingMethods.getAll().size());

      javax.json.JsonArrayBuilder loggingMethodsBuilder = JsonWriter.get(loggingMethods);
      File file = new File("C:/Users/jacob/dev/Petroware/PWLS/loggingMethods.json");
      java.io.FileOutputStream outputStream = new java.io.FileOutputStream(file);
      JsonWriter.save(outputStream, loggingMethodsBuilder.build());
      outputStream.close();

      System.out.println("Complete writing JSON: " + file);

      LoggingMethods loggingMethods2 = JsonReader.readLoggingMethods(file);
      System.out.println(loggingMethods2);
      */

      /* CURVES BY TOOL
      System.out.println("READ TOOLS from EXCEL");
      Tools tools = no.geosoft.jpwls.excel.ExcelReader.readTools(logsFile);

      System.out.println("READ CURVES from EXCEL");
      Curves curves = no.geosoft.jpwls.excel.ExcelReader.readCurves(logsFile);

      System.out.println("READ CURVES WITHIN TOOLS from EXCEL");
      no.geosoft.jpwls.excel.ExcelReader.readCurvesByTool(logsFile, tools, curves);

      System.out.println("WRITE curvesByTool.json");
      javax.json.JsonArrayBuilder curvesByToolBuilder = JsonWriter.getCurves(tools);
      File file = new File("C:/Users/jacob/dev/Petroware/PWLS/curvesByTool.json");
      java.io.FileOutputStream outputStream = new java.io.FileOutputStream(file);
      JsonWriter.save(outputStream, curvesByToolBuilder.build());
      outputStream.close();

      System.out.println("READ TOOLS from JSON");
      File toolsFile = new File("C:/Users/jacob/dev/Petroware/PWLS/tools.json");
      Tools tools2 = JsonReader.readTools(toolsFile);

      System.out.println("READ CURVES from JSON");
      File curvesFile = new File("C:/Users/jacob/dev/Petroware/PWLS/curves.json");
      Curves curves2 = JsonReader.readCurves(curvesFile);

      System.out.println("READ CUVRES BY TOOLS from JSON");
      JsonReader.readCurvesByTool(file, tools2, curves2);

      System.out.println(tools2);
      */

      String url = BASE_URL + "/standard/companies.json";
      InputStream stream = new java.net.URL(url).openStream();
      Companies companies = JsonReader.readCompanies(stream);
      System.out.println(companies);
    }
    catch (Exception exception) {
      exception.printStackTrace();
    }
  }
}


