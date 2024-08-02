package no.geosoft.jpwls.excel;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

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
 * Reader for PWLS Excel definition.
 *
 * @author <a href="mailto:jacob.dreyer@geosoft.no">Jacob Dreyer</a>
 */
public final class ExcelReader
{
  /** The logger instance. */
  private static final Logger logger_ = Logger.getLogger(ExcelReader.class.getName());

  /**
   * Private constructor to prevent client instantiation.
   */
  private ExcelReader()
  {
    assert false : "This constructor should never be called";
  }

  /**
   * Return the <em>value</em> of the specified cell as a string.
   *
   * @param cell  Cell to investigate. Null in case it doesn't contain anything.
   * @return      Cell value as a string. Never null.
   */
  private static String getCellValueAsString(XSSFCell cell)
  {
    String cellValue;

    if (cell == null)
      cellValue = "";
    else if (cell.getCellType() == Cell.CELL_TYPE_STRING)
      cellValue = new String(cell.getStringCellValue().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
    else
      cellValue = cell.toString();

    return cellValue.trim();
  }

  /**
   * Return the <em>value</em> of the specified cell as an integer.
   *
   * @param cell  Cell to investigate. Null in case it doesn't contain anything.
   * @return      Cell value as an integer. Null if cell is empty or doesn't an
   *              integer value.
   */
  private static Integer getCellValueAsInteger(XSSFCell cell)
  {
    String text = getCellValueAsString(cell);

    try {
      double value = Double.parseDouble(text);
      return (int) value;
    }
    catch (NumberFormatException exception) {
      logger_.log(Level.WARNING, "Non-integer: \"" + text + "\"", exception);
      return null;
    }
  }

  /**
   * Return the <em>value</em> of the specified cell as a boolean.
   *
   * @param cell  Cell to investigate. Null in case it doesn't contain anything.
   * @return      Cell value as a boolean.
   */
  private static boolean getCellValueAsBoolean(XSSFCell cell)
  {
    String text = getCellValueAsString(cell).toLowerCase();
    return text.equals("true");
  }

  /**
   * Read tools/curves mapping from PWLS Excel sheet "Curves Within Tools".
   *
   * @param file    The MS/Excel file to read from. Non-null.
   * @param tools   The tools instance to modify. Non-null.
   * @param curves  The curves instance to pick curves from. Non-null.
   * @throws IllegalArgumentException  If tools or curves is null.
   * @throws IOException  If the read opertaion fails for some reason.
   */
  public static void readCurvesOfTools(InputStream stream, Tools tools, Curves curves)
    throws IOException
  {
    if (stream == null)
      throw new IllegalArgumentException("stream cannot be null");

    if (tools == null)
      throw new IllegalArgumentException("tools cannot be null");

    if (curves == null)
      throw new IllegalArgumentException("curves cannot be null");

    XSSFWorkbook workbook = new XSSFWorkbook(stream);
    XSSFSheet sheet = workbook.getSheet("Curves Within Tools");

    logger_.log(Level.INFO, "Reading \"Curves Within Tools\" from " + stream);

    // Get row iterator and skip the first (header) row
    Iterator<Row> rowIterator = sheet.rowIterator();
    if (rowIterator.hasNext())
      rowIterator.next();

    while (rowIterator.hasNext()) {
      XSSFRow row = (XSSFRow) rowIterator.next();

      XSSFCell companyCodeCell = row.getCell(0); // A
      Integer companyCode = getCellValueAsInteger(companyCodeCell);

      XSSFCell toolCodeCell = row.getCell(1); // B
      String toolCode = getCellValueAsString(toolCodeCell);

      XSSFCell curveMnemonicCell = row.getCell(2); // C
      String curveMnemonic = getCellValueAsString(curveMnemonicCell);

      Tool tool = tools.get(toolCode, companyCode);
      if (tool == null)
        logger_.log(Level.WARNING, "Unknown tool: " + toolCode + " for company=" + companyCode);

      Curve curve = curves.get(curveMnemonic, companyCode);
      if (curve == null)
        logger_.log(Level.WARNING, "Unknown curve: " + curveMnemonic + " for company=" + companyCode);

      if (tool != null && curve != null)
        tool.addCurve(curve);
    }

    try {
      workbook.close();
    }
    catch (IOException exception) {
      logger_.log(Level.WARNING, "Unable to close workbook: " + stream, exception);
    }
  }

  /**
   * Read tools/curves mapping from PWLS Excel sheet "Curves Within Tools" of the
   * specified file.
   *
   * @param file    The MS/Excel file to read from. Non-null.
   * @param tools   The tools instance to modify. Non-null.
   * @param curves  The curves instance to pick curves from. Non-null.
   * @throws IllegalArgumentException  If tools or curves is null.
   * @throws IOException  If the read opertaion fails for some reason.
   */
  public static void readCurvesOfTools(File file, Tools tools, Curves curves)
    throws IOException
  {
    FileInputStream inputStream = null;
    try {
      inputStream = new FileInputStream(file);
      readCurvesOfTools(inputStream, tools, curves);
    }
    finally {
      if (inputStream != null)
        inputStream.close();
    }
  }

  /**
   * Read curves from PWLS Excel sheet "Curves".
   *
   * @param file  The MS/Excel file to read from. Non-null.
   * @return  The requested curves. Never null.
   * @throws  IOException  If the read opertaion fails for some reason.
   */
  public static Curves readCurves(InputStream stream)
    throws IOException
  {
    if (stream == null)
      throw new IllegalArgumentException("stream cannot be null");

    Curves curves = new Curves();

    XSSFWorkbook workbook = new XSSFWorkbook(stream);
    XSSFSheet sheet = workbook.getSheet("Curves");

    logger_.log(Level.INFO, "Reading \"Curves\" from " + stream);

    // Get row iterator and skip the first (header) row
    Iterator<Row> rowIterator = sheet.rowIterator();
    if (rowIterator.hasNext())
      rowIterator.next();

    int nRows = 0;
    while (rowIterator.hasNext()) {
      XSSFRow row = (XSSFRow) rowIterator.next();

      XSSFCell companyCodeCell = row.getCell(0); // A
      Integer companyCode = getCellValueAsInteger(companyCodeCell);

      XSSFCell mnemonicCell = row.getCell(1); // B
      String mnemonic = getCellValueAsString(mnemonicCell);

      XSSFCell propertyCell = row.getCell(2); // C
      String property = getCellValueAsString(propertyCell);

      XSSFCell quantityCell = row.getCell(3); // D
      String quantity = getCellValueAsString(quantityCell);

      XSSFCell lisMnemonicCell = row.getCell(4); // E
      String lisMnemonic = getCellValueAsString(lisMnemonicCell);

      XSSFCell descriptionCell = row.getCell(5); // F
      String description = getCellValueAsString(descriptionCell);

      Curve curve = new Curve(mnemonic,
                              lisMnemonic,
                              companyCode,
                              property,
                              quantity,
                              description);

      curves.add(curve);
      nRows++;
    }

    logger_.log(Level.INFO, "Read " + nRows + " from \"Curves\".");

    try {
      workbook.close();
    }
    catch (IOException exception) {
      logger_.log(Level.WARNING, "Unable to close workbook: " + stream, exception);
    }

    return curves;
  }

  /**
   * Read curves from PWLS Excel sheet "Curves" of the specified file.
   *
   * @param file  The MS/Excel file to read from. Non-null.
   * @return  The requested curves. Never null.
   * @throws  IOException  If the read opertaion fails for some reason.
   */
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
   * Read tools from PWLS Excel sheet "Tools".
   *
   * @param stream  The MS/Excel stream to read from. Non-null.
   * @return  The requested tools. Never null.
   * @throws  IOException  If the read opertaion fails for some reason.
   */
  public static Tools readTools(InputStream stream)
    throws IOException
  {
    if (stream == null)
      throw new IllegalArgumentException("stream cannot be null");

    Tools tools = new Tools();

    XSSFWorkbook workbook = new XSSFWorkbook(stream);
    XSSFSheet sheet = workbook.getSheet("Tools");

    logger_.log(Level.INFO, "Reading \"Tools\" from" + stream);

    // Get row iterator and skip the first (header) row
    Iterator<Row> rowIterator = sheet.rowIterator();
    if (rowIterator.hasNext())
      rowIterator.next();

    int nRows = 0;
    while (rowIterator.hasNext()) {
      XSSFRow row = (XSSFRow) rowIterator.next();

      XSSFCell companyCodeCell = row.getCell(0); // A
      Integer companyCode = getCellValueAsInteger(companyCodeCell);

      XSSFCell codeCell = row.getCell(1); // B
      String code = getCellValueAsString(codeCell);

      XSSFCell groupCell = row.getCell(2); // C
      String group = getCellValueAsString(groupCell);

      XSSFCell marketingNameCell = row.getCell(3); // D
      String marketingName = getCellValueAsString(marketingNameCell);

      XSSFCell descriptionCell = row.getCell(4); // E
      String description = getCellValueAsString(descriptionCell);

      XSSFCell genericTypeCell = row.getCell(5); // F
      String genericType = getCellValueAsString(genericTypeCell);

      XSSFCell loggingMethodCell = row.getCell(6); // G
      String loggingMethod = getCellValueAsString(loggingMethodCell);

      XSSFCell typeDescriptionCell = row.getCell(7); // H
      String typeDescription = getCellValueAsString(typeDescriptionCell);

      Tool tool = new Tool(code,
                           companyCode,
                           group,
                           marketingName,
                           description,
                           genericType,
                           loggingMethod,
                           typeDescription);

      tools.add(tool);
      nRows++;
    }

    logger_.log(Level.INFO, "Read " + nRows + " from \"Tools\".");

    try {
      workbook.close();
    }
    catch (IOException exception) {
      logger_.log(Level.WARNING, "Unable to close workbook: " + stream, exception);
    }

    return tools;
  }

  /**
   * Read tools from PWLS Excel sheet "Tools" of the specified file.
   *
   * @param file  The MS/Excel file to read from. Non-null.
   * @return  The requested tools. Never null.
   * @throws  IOException  If the read opertaion fails for some reason.
   */
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
   * Read companies from PWLS Excel sheet "Company Codes".
   *
   * @param stream  The MS/Excel input stream to read from. Non-null.
   * @return  The requested companies. Never null.
   * @throws  IOException  If the read operation fails for some reason.
   */
  public static Companies readCompanies(InputStream stream)
    throws IOException
  {
    if (stream == null)
      throw new IllegalArgumentException("stream cannot be null");

    Companies companies = new Companies();

    XSSFWorkbook workbook = new XSSFWorkbook(stream);
    XSSFSheet sheet = workbook.getSheet("Company Codes");

    logger_.log(Level.INFO, "Reading \"Company Codes\" from " + stream);

    // Get row iterator and skip the first (header) row
    Iterator<Row> rowIterator = sheet.rowIterator();
    if (rowIterator.hasNext())
      rowIterator.next();

    int nRows = 0;
    while (rowIterator.hasNext()) {
      XSSFRow row = (XSSFRow) rowIterator.next();

      XSSFCell companyCodeCell = row.getCell(0); // A
      Integer companyCode = getCellValueAsInteger(companyCodeCell);
      if (companyCode == null) {
        logger_.log(Level.WARNING, "Unexpected company code: " + getCellValueAsString(companyCodeCell) + ". Skipping.");
        continue;
      }

      Company existingCompany = companies.get(companyCode);
      if (existingCompany != null) {
        logger_.log(Level.WARNING, "Company already exists for company code=" + companyCode + ": " + existingCompany);
        continue;
      }

      XSSFCell nameCell = row.getCell(1); // B
      String name = getCellValueAsString(nameCell);
      if (name.isEmpty()) {
        logger_.log(Level.WARNING, "Company name not specified: " + companyCode + ". Skipping entry.");
        continue;
      }

      Company company = new Company(companyCode, name);
      companies.add(company);

      nRows++;
    }

    logger_.log(Level.INFO, "Read " + nRows + " from \"Company Codes\".");

    try {
      workbook.close();
    }
    catch (IOException exception) {
      logger_.log(Level.WARNING, "Unable to close workbook: " + stream, exception);
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
   * Read logging methods from PWLS Excel sheet "Logging Method".
   *
   * @param file  The MS/Excel file to read from. Non-null.
   * @return  The requested logging methods. Never null.
   * @throws  IOException  If the read opertaion fails for some reason.
   */
  public static LoggingMethods readLoggingMethods(InputStream stream)
    throws IOException
  {
    if (stream == null)
      throw new IllegalArgumentException("stream cannot be null");

    LoggingMethods loggingMethods = new LoggingMethods();

    XSSFWorkbook workbook = new XSSFWorkbook(stream);

    XSSFSheet sheet = workbook.getSheet("Logging Method");

    logger_.log(Level.INFO, "Reading \"Logging Method\" from " + stream);

    // Get row iterator and skip the first (header) row
    Iterator<Row> rowIterator = sheet.rowIterator();
    if (rowIterator.hasNext())
      rowIterator.next();

    int nRows = 0;
    while (rowIterator.hasNext()) {
      XSSFRow row = (XSSFRow) rowIterator.next();

      XSSFCell nameCell = row.getCell(0); // A
      String name = getCellValueAsString(nameCell);

      XSSFCell descriptionCell = row.getCell(1); // B
      String description = getCellValueAsString(descriptionCell);

      LoggingMethod loggingMethod = new LoggingMethod(name, description);
      loggingMethods.add(loggingMethod);

      nRows++;
    }

    logger_.log(Level.INFO, "Read " + nRows + " from \"Logging Method\".");

    try {
      workbook.close();
    }
    catch (IOException exception) {
      logger_.log(Level.WARNING, "Unable to close workbook: " + stream, exception);
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

  /**
   * Read tool classes from PWLS Excel sheet "Well Log Tool Class".
   *
   * @param stream  The MS/Excel stream to read from. Non-null.
   * @return  The requested tool classes. Never null.
   * @throws  IOException  If the read opertaion fails for some reason.
   */
  public static ToolClasses readToolClasses(InputStream stream)
    throws IOException
  {
    if (stream == null)
      throw new IllegalArgumentException("stream cannot be null");

    ToolClasses toolClasses = new ToolClasses();

    XSSFWorkbook workbook = new XSSFWorkbook(stream);
    XSSFSheet sheet = workbook.getSheet("Well Log Tool Class");

    logger_.log(Level.INFO, "Reading \"well Log Tool Class\" from " + stream);

    // Get row iterator and skip the first (header) row
    Iterator<Row> rowIterator = sheet.rowIterator();
    if (rowIterator.hasNext())
      rowIterator.next();

    int nRows = 0;
    while (rowIterator.hasNext()) {
      XSSFRow row = (XSSFRow) rowIterator.next();

      XSSFCell nameCell = row.getCell(0); // A
      String name = getCellValueAsString(nameCell);

      XSSFCell descriptionCell = row.getCell(1); // B
      String description = getCellValueAsString(descriptionCell);

      ToolClass toolClass = new ToolClass(name, description);
      toolClasses.add(toolClass);

      nRows++;
    }

    logger_.log(Level.INFO, "Read " + nRows + " from \"Well Log Tool Class\".");

    try {
      workbook.close();
    }
    catch (IOException exception) {
      logger_.log(Level.WARNING, "Unable to close workbook: " + stream, exception);
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
   * Read properties from PWLS Excel sheet.
   *
   * @param stream  The MS/Excel stream to read from. Non-null.
   * @return  The requested companies. Never null.
   * @throws  IOException  If the read opertaion fails for some reason.
   */
  public static Properties readProperties(InputStream stream)
    throws IOException
  {
    if (stream == null)
      throw new IllegalArgumentException("stream cannot be null");

    Properties properties = new Properties();

    XSSFWorkbook workbook = new XSSFWorkbook(stream);
    XSSFSheet sheet = workbook.getSheet("Properties");

    logger_.log(Level.INFO, "Reading \"Properties\" from " + stream);

    // Get row iterator and skip the first (header) row
    Iterator<Row> rowIterator = sheet.rowIterator();
    if (rowIterator.hasNext())
      rowIterator.next();

    // Temporary holder of parents
    Map<Property,String> parents = new HashMap<>();

    int nRows = 0;
    while (rowIterator.hasNext()) {
      XSSFRow row = (XSSFRow) rowIterator.next();

      XSSFCell sortOrderCell = row.getCell(0); // A
      Integer sortOrder = getCellValueAsInteger(sortOrderCell);
      if (sortOrder == null) {
        logger_.log(Level.WARNING, "Unexpected sortOrder: \"" + getCellValueAsString(sortOrderCell) + "\". Skip entry.");
        continue;
      }

      XSSFCell propertyNameCell = row.getCell(1); // B
      String propertyName = getCellValueAsString(propertyNameCell);

      XSSFCell parentPropertyCell = row.getCell(2); // C
      String parentPropertyName = getCellValueAsString(parentPropertyCell);

      XSSFCell descriptionCell = row.getCell(3); // D
      String description = getCellValueAsString(descriptionCell);

      XSSFCell abstractCell = row.getCell(4); // E
      boolean isAbstract = getCellValueAsBoolean(abstractCell);

      XSSFCell quantityCell = row.getCell(5); // F
      String quantity = getCellValueAsString(quantityCell);

      XSSFCell guidCell = row.getCell(9); // J
      String guid = getCellValueAsString(guidCell);

      XSSFCell parentGuidCell = row.getCell(10); // K
      String parentGuid = getCellValueAsString(parentGuidCell);

      Property property = new Property(propertyName,
                                       description,
                                       quantity,
                                       guid,
                                       sortOrder,
                                       isAbstract);

      parents.put(property, parentGuid);

      properties.add(property);

      nRows++;
    }

    // Resolve parents
    for (Property property : properties.getAll()) {
      String parentGuid = parents.get(property);
      Property parentProperty = properties.findByGuid(parentGuid);
      if (parentProperty == null)
        logger_.log(Level.WARNING, "Missing parent property for " + property.getName());

      // Keep parent == null if at root level
      if (parentProperty != property)
        property.setParent(parentProperty);
    }

    logger_.log(Level.INFO, "Read " + nRows + " from \"Properties\".");

    try {
      workbook.close();
    }
    catch (IOException exception) {
      logger_.log(Level.WARNING, "Unable to close workbook: " + stream, exception);
    }

    return properties;
  }

  /**
   * Read properties from PWLS Excel sheet.
   *
   * @param file  The MS/Excel file to read from. Non-null.
   * @return  The requested companies. Never null.
   * @throws  IOException  If the read opertaion fails for some reason.
   */
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
   * Testing this class.
   *
   * @param arguments  Application arguments. Not used.
   */
  public static void main(String[] arguments)
  {
    String BASE_URL = "https://raw.githubusercontent.com/geosoft-as/pwls/main";

    String pwlsUrl = BASE_URL + "/excel/PWLS_v3.0_Logs.xlsx";
    String propertiesUrl = BASE_URL + "/excel/PWLS_v3.0_Properties.xlsx";

    java.io.InputStream stream;

    try {
      System.out.println("===TOOL CLASSES========");
      stream = new java.net.URL(pwlsUrl).openStream();
      ToolClasses toolClasses = ExcelReader.readToolClasses(stream);
      System.out.println(no.geosoft.jpwls.json.JsonWriter.toString(no.geosoft.jpwls.json.JsonWriter.getToolClasses(toolClasses.getAll()).build()));
      stream.close();

      /*
      System.out.println("===COMPANIES===========");
      stream = new java.net.URL(pwlsUrl).openStream();
      Companies companies = ExcelReader.readCompanies(stream);
      System.out.println(no.geosoft.jpwls.json.JsonWriter.toString(no.geosoft.jpwls.json.JsonWriter.get(companies).build()));
      stream.close();

      System.out.println("===TOOLS===============");
      stream = new java.net.URL(pwlsUrl).openStream();
      Tools tools = ExcelReader.readTools(stream);
      System.out.println(no.geosoft.jpwls.json.JsonWriter.toString(no.geosoft.jpwls.json.JsonWriter.get(tools).build()));
      stream.close();

      System.out.println("===CURVES==============");
      stream = new java.net.URL(pwlsUrl).openStream();
      Curves curves = ExcelReader.readCurves(stream);
      System.out.println(no.geosoft.jpwls.json.JsonWriter.toString(no.geosoft.jpwls.json.JsonWriter.get(curves).build()));
      stream.close();

      System.out.println("===CURVES/TOOLS========");
      stream = new java.net.URL(pwlsUrl).openStream();
      ExcelReader.readCurvesWithinTools(stream, tools, curves);
      System.out.println(no.geosoft.jpwls.json.JsonWriter.toString(no.geosoft.jpwls.json.JsonWriter.get(tools).build()));
      stream.close();
      */

      /*
      System.out.println("===PROPERTIES==========");
      stream = new java.net.URL(propertiesUrl).openStream();
      Properties properties = ExcelReader.readProperties(stream);
      System.out.println(no.geosoft.jpwls.json.JsonWriter.toString(no.geosoft.jpwls.json.JsonWriter.get(properties).build()));
      stream.close();
      */
    }
    catch (IOException exception) {
      exception.printStackTrace();
    }
  }
}
