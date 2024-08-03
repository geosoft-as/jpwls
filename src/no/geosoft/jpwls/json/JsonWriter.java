package no.geosoft.jpwls.json;

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
 * Class for writing PWLS instances as JSON.
 *
 * @author <a href="mailto:jacob.dreyer@geosoft.no">Jacob Dreyer</a>
 */
public final class JsonWriter
{
  /**
   * Private constructor to prevent client instantiation.
   */
  private JsonWriter()
  {
    assert false : "This constructor should never be called";
  }

  /**
   * Save the specified JSON structure to the given stream.
   *
   * @param stream  Stream to save to. Non-null.
   * @param json  JSON structure to save. Non-null.
   * @throws IllegalargumentException  If stream of json is null.
   * @throws IOException  If the save operation fails for some reason.
   */
  public static void save(OutputStream stream, JsonStructure json)
    throws IOException
  {
    if (stream == null)
      throw new IllegalArgumentException("stream cannot be null");

    if (json == null)
      throw new IllegalArgumentException("json cannot be null");

    Map<String, Object> config = new HashMap<>();
    config.put(JsonGenerator.PRETTY_PRINTING, true);

    Writer writer = new OutputStreamWriter(stream, StandardCharsets.UTF_8);

    JsonWriterFactory jsonWriterFactory = Json.createWriterFactory(config);
    javax.json.JsonWriter jsonWriter = jsonWriterFactory.createWriter(writer);

    jsonWriter.write(json);
    jsonWriter.close();
  }

  /**
   * Save the specified JSON structure to the given file.
   *
   * @param file  File to save to. Non-null.
   * @param json  JSON structure to save. Non-null.
   * @throws IllegalargumentException  If file of json is null.
   * @throws IOException  If the save operation fails for some reason.
   */
  public static void save(File file, JsonStructure json)
    throws IOException
  {
    if (file == null)
      throw new IllegalArgumentException("file cannot be null");

    if (json == null)
      throw new IllegalArgumentException("json cannot be null");

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

  /**
   * Return the specified JSON structure as a pretty-printed JSON string.
   *
   * @param json  JSON structure to consider. Non-null.
   * @return      The equivalent pretty-printed JSON string. Never null.
   */
  public static String toString(JsonStructure json)
  {
    if (json == null)
      throw new IllegalArgumentException("json cannot be null");

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

  /**
   * Return the specified PWLS curve as a JSON object builder.
   *
   * @param curve  Curve to consider. Non-null.
   * @return       The equivalent JSON object builder. Never null.
   * @throws IllegalArgumentException  If curve is null.
   */
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

  /**
   * Return the specified PWLS curves as a JSON array builder.
   *
   * @param curves  Curves to consider. Non-null.
   * @return        The equivalent JSON array builder. Never null.
   * @throws IllegalArgumentException  If curves is null.
   */
  public static JsonArrayBuilder getCurves(Set<Curve> curves)
  {
    if (curves == null)
      throw new IllegalArgumentException("curves cannot be null");

    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
    for (Curve curve : curves)
      arrayBuilder.add(getCurve(curve));

    return arrayBuilder;
  }

  /**
   * Return the specified PWLS tool as a JSON object builder.
   *
   * @param tool  Tool to consider. Non-null.
   * @return      The equivalent JSON object builder. Never null.
   * @throws IllegalArgumentException  If tool is null.
   */
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

  /**
   * Return the specified PWLS tools as a JSON array builder.
   *
   * @param tools  Tools to consider. Non-null.
   * @return       The equivalent JSON array builder. Never null.
   * @throws IllegalArgumentException  If tools is null.
   */
  public static JsonArrayBuilder getTools(Set<Tool> tools)
  {
    if (tools == null)
      throw new IllegalArgumentException("tools cannot be null");

    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
    for (Tool tool : tools)
      arrayBuilder.add(getTool(tool));

    return arrayBuilder;
  }

  /**
   * Return the specified PWLS company as a JSON object builder.
   *
   * @param company  Company to consider. Non-null.
   * @return         The equivalent JSON object builder. Never null.
   * @throws IllegalArgumentException  If company is null.
   */
  public static JsonObjectBuilder getCompany(Company company)
  {
    if (company == null)
      throw new IllegalArgumentException("company cannot be null");

    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
    objectBuilder.add("code", company.getCode());
    objectBuilder.add("name", company.getName());
    return objectBuilder;
  }

  /**
   * Return the specified PWLS companies as a JSON array builder.
   *
   * @param companies  Companies to consider. Non-null.
   * @return           The equivalent JSON array builder. Never null.
   * @throws IllegalArgumentException  If companies is null.
   */
  public static JsonArrayBuilder getCompanies(Set<Company> companies)
  {
    if (companies == null)
      throw new IllegalArgumentException("companies cannot be null");

    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
    for (Company company : companies)
      arrayBuilder.add(getCompany(company));

    return arrayBuilder;
  }

  /**
   * Return the specified PWLS property as a JSON object builder.
   *
   * @param property  Property to consider. Non-null.
   * @return          The equivalent JSON object builder. Never null.
   * @throws IllegalArgumentException  If property is null.
   */
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

  /**
   * Return the specified PWLS properties as a JSON array builder.
   *
   * @param properties  Properties to consider. Non-null.
   * @return            The equivalent JSON array builder. Never null.
   * @throws IllegalArgumentException  If properties is null.
   */
  public static JsonArrayBuilder getProperties(Set<Property> properties)
  {
    if (properties == null)
      throw new IllegalArgumentException("properties cannot be null");

    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
    for (Property property : properties)
      arrayBuilder.add(getProperty(property));

    return arrayBuilder;
  }

  /**
   * Return the specified PWLS tool class as a JSON object builder.
   *
   * @param toolClass  Tool class to consider. Non-null.
   * @return           The equivalent JSON object builder. Never null.
   * @throws IllegalArgumentException  If toolClass is null.
   */
  public static JsonObjectBuilder getToolClass(ToolClass toolClass)
  {
    if (toolClass == null)
      throw new IllegalArgumentException("toolClass cannot be null");

    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
    objectBuilder.add("name", toolClass.getName());
    objectBuilder.add("description", toolClass.getDescription());
    return objectBuilder;
  }

  /**
   * Return the specified PWLS tool classes as a JSON array builder.
   *
   * @param toolClasses  Tool classes to consider. Non-null.
   * @return             The equivalent JSON array builder. Never null.
   * @throws IllegalArgumentException  If toolClasses is null.
   */
  public static JsonArrayBuilder getToolClasses(Set<ToolClass> toolClasses)
  {
    if (toolClasses == null)
      throw new IllegalArgumentException("toolClasses cannot be null");

    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
    for (ToolClass toolClass : toolClasses)
      arrayBuilder.add(getToolClass(toolClass));

    return arrayBuilder;
  }

  /**
   * Return the specified PWLS logging method as a JSON object builder.
   *
   * @param loggingMethod  Logging method to consider. Non-null.
   * @return               The equivalent JSON object builder. Never null.
   * @throws IllegalArgumentException  If loggingMethod is null.
   */
  public static JsonObjectBuilder getLoggingMethod(LoggingMethod loggingMethod)
  {
    if (loggingMethod == null)
      throw new IllegalArgumentException("loggingMethod cannot be null");

    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
    objectBuilder.add("name", loggingMethod.getName());
    objectBuilder.add("description", loggingMethod.getDescription());
    return objectBuilder;
  }

  /**
   * Return the specified PWLS logging methods as a JSON array builder.
   *
   * @param loggingMethods  Logging methods to consider. Non-null.
   * @return                The equivalent JSON array builder. Never null.
   * @throws IllegalArgumentException  If loggingMethods is null.
   */
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
}
