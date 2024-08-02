package no.geosoft.pwls.json;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.io.Writer;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.nio.charset.StandardCharsets;

import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.Json;
import javax.json.JsonStructure;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;

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

public final class JsonWriter
{
  private JsonWriter()
  {
    assert false : "This constructor should never be called";
  }

  public static void save(File file, JsonStructure json)
    throws IOException
  {
    FileOutputStream fileStream = new FileOutputStream(file);
    try {
      JsonWriter.save(fileStream, json);
      fileStream.close();
    }
    catch (Exception exception) {
      throw exception;
    }
    finally {
      fileStream.close();
    }
  }

  public static void save(OutputStream outputStream, JsonStructure json)
    throws IOException
  {
    Map<String, Object> config = new HashMap<>();
    config.put(JsonGenerator.PRETTY_PRINTING, true);

    Writer writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);

    JsonWriterFactory jsonWriterFactory = Json.createWriterFactory(config);
    javax.json.JsonWriter jsonWriter = jsonWriterFactory.createWriter(writer);

    jsonWriter.write(json);
    jsonWriter.close();
  }


  public static String toString(JsonStructure json)
  {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(byteArrayOutputStream);

    try {
      save(printStream, json);
      return new String(byteArrayOutputStream.toByteArray(), StandardCharsets.UTF_8);
    }
    catch (IOException exception) {
      assert false;
      return null;
    }
  }

  public static JsonObjectBuilder getCurve(Curve curve)
  {
    if (curve == null)
      throw new IllegalArgumentException("curve cannot be null");

    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
    objectBuilder.add("mnemonic", curve.getMnemonic());
    objectBuilder.add("shortMnemonic", curve.getLisMnemonic());
    objectBuilder.add("companyCode", curve.getCompanyCode());
    objectBuilder.add("property", curve.getProperty());
    objectBuilder.add("quantity", curve.getQuantity());
    objectBuilder.add("description", curve.getDescription());
    return objectBuilder;
  }

  public static JsonArrayBuilder getCurves(Set<Curve> curves)
  {
    if (curves == null)
      throw new IllegalArgumentException("curves cannot be null");

    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
    for (Curve curve : curves)
      arrayBuilder.add(getCurve(curve));

    return arrayBuilder;
  }

  public static JsonObjectBuilder getTool(Tool tool)
  {
    if (tool == null)
      throw new IllegalArgumentException("tool cannot be null");

    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
    objectBuilder.add("code", tool.getCode());
    objectBuilder.add("companyCode", tool.getCompanyCode());
    objectBuilder.add("group", tool.getGroup());
    objectBuilder.add("marketingName", tool.getMarketingName());
    objectBuilder.add("description", tool.getDescription());
    objectBuilder.add("genericType", tool.getGenericType());
    objectBuilder.add("loggingMethod", tool.getLoggingMethod());
    objectBuilder.add("typeDescription", tool.getTypeDescription());
    return objectBuilder;
  }

  public static JsonArrayBuilder getTools(Set<Tool> tools)
  {
    if (tools == null)
      throw new IllegalArgumentException("tools cannot be null");

    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
    for (Tool tool : tools)
      arrayBuilder.add(getTool(tool));

    return arrayBuilder;
  }

  public static JsonObjectBuilder getCompany(Company company)
  {
    if (company == null)
      throw new IllegalArgumentException("company cannot be null");

    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
    objectBuilder.add("code", company.getCode());
    objectBuilder.add("name", company.getName());
    return objectBuilder;
  }

  public static JsonArrayBuilder getCompanies(Set<Company> companies)
  {
    if (companies == null)
      throw new IllegalArgumentException("companies cannot be null");

    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
    for (Company company : companies)
      arrayBuilder.add(getCompany(company));

    return arrayBuilder;
  }

  public static JsonObjectBuilder getProperty(Property property)
  {
    if (property == null)
      throw new IllegalArgumentException("property cannot be null");

    String parentName = property.getParent() != null ? property.getParent().getName() : null;

    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
    objectBuilder.add("name", property.getName());
    objectBuilder.add("description", property.getDescription());
    objectBuilder.add("quantity", property.getQuantity());
    objectBuilder.add("guid", property.getGuid());
    if (parentName != null)
      objectBuilder.add("parent", parentName);
    else
      objectBuilder.addNull("parent");
    objectBuilder.add("sortOrder", property.getSortOrder());
    objectBuilder.add("isAbstract", property.isAbstract());

    return objectBuilder;
  }

  public static JsonArrayBuilder getProperties(Set<Property> properties)
  {
    if (properties == null)
      throw new IllegalArgumentException("properties cannot be null");

    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
    for (Property property : properties)
      arrayBuilder.add(getProperty(property));

    return arrayBuilder;
  }

  public static JsonObjectBuilder getToolClass(ToolClass toolClass)
  {
    if (toolClass == null)
      throw new IllegalArgumentException("toolClass cannot be null");

    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
    objectBuilder.add("name", toolClass.getName());
    objectBuilder.add("description", toolClass.getDescription());
    return objectBuilder;
  }

  public static JsonArrayBuilder getToolClasses(Set<ToolClass> toolClasses)
  {
    if (toolClasses == null)
      throw new IllegalArgumentException("toolClasses cannot be null");

    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
    for (ToolClass toolClass : toolClasses)
      arrayBuilder.add(getToolClass(toolClass));

    return arrayBuilder;
  }

  public static JsonObjectBuilder getLoggingMethod(LoggingMethod loggingMethod)
  {
    if (loggingMethod == null)
      throw new IllegalArgumentException("loggingMethod cannot be null");

    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
    objectBuilder.add("name", loggingMethod.getName());
    objectBuilder.add("description", loggingMethod.getDescription());
    return objectBuilder;
  }

  public static JsonArrayBuilder getLoggingMethods(Set<LoggingMethod> loggingMethods)
  {
    if (loggingMethods == null)
      throw new IllegalArgumentException("loggingMethods cannot be null");

    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
    for (LoggingMethod loggingMethod : loggingMethods)
      arrayBuilder.add(getLoggingMethod(loggingMethod));

    return arrayBuilder;
  }

  public static JsonArrayBuilder getCurves(Tools tools)
  {
    if (tools == null)
      throw new IllegalArgumentException("tools cannot be null");

    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
    for (Tool tool : tools.getAll()) {
      for (Curve curve : tool.getCurves()) {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        objectBuilder.add("toolCode", tool.getCode());
        objectBuilder.add("companyCode", tool.getCompanyCode());
        objectBuilder.add("curveMnemonic", curve.getMnemonic());
        arrayBuilder.add(objectBuilder);
      }
    }

    return arrayBuilder;
  }

  public static void main(String[] arguments)
  {
    String BASE_URL = "https://raw.githubusercontent.com/rabbagast/pwls/main";

    File logsFile = new File("C:/Users/jacob/dev/Petroware/PWLS/PWLS v3.0 Logs.xlsx");
    File propertiesFile = new File("C:/Users/jacob/dev/Petroware/PWLS/PWLS v3.0 Properties.xlsx");

    try {
      // Companies
      String url = BASE_URL + "/standard/PWLS_v3.0_Logs.xlsx";
      java.io.InputStream stream = new java.net.URL(url).openStream();
      Companies companies = no.geosoft.jpwls.excel.ExcelReader.readCompanies(stream);
      JsonArrayBuilder arrayBuilder = JsonWriter.getCompanies(companies.getAll());
      System.out.println(JsonWriter.toString(arrayBuilder.build()));

      //ToolClasses toolClasses = no.geosoft.jpwls.excel.ExcelReader.readToolClasses(logsFile);
      //JsonArrayBuilder arrayBuilder = JsonWriter.get(toolClasses);

      /*
      Tools tools = no.geosoft.jpwls.excel.ExcelReader.readTools(logsFile);
      Curves curves = no.geosoft.jpwls.excel.ExcelReader.readCurves(logsFile);
      no.geosoft.jpwls.excel.ExcelReader.readCurvesWithinTools(logsFile, tools, curves);

      JsonArrayBuilder arrayBuilder = getCurves(tools);
      System.out.println(JsonWriter.toString(arrayBuilder.build()));
      */

      /*
      File file = new File("C:/Users/jacob/companies.json");
      FileOutputStream fileOutputStream = new FileOutputStream(file);
      JsonWriter.save(fileOutputStream, arrayBuilder.build());
      */

      // System.out.println(JsonWriter.toString(arrayBuilder.build()));
    }
    catch (IOException exception) {
      exception.printStackTrace();
    }

  }
}
