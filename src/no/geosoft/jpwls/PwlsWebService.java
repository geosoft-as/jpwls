package no.geosoft.jpwls;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import no.geosoft.jpwls.json.JsonWriter;
import no.geosoft.jpwls.util.Query;

/**
 * The PWLS web service.
 *
 * @author <a href="mailto:jacob.dreyer@geosoft.no">Jacob Dreyer</a>
 */
public final class PwlsWebService implements HttpHandler
{
  /** Base URL for PWLS JSON files. */
  private static final String BASE_URL = "https://raw.githubusercontent.com/geosoft-as/pwls/main/json";

  /** The logging instance. */
  private static final Logger logger_ = Logger.getLogger(PwlsWebService.class.getName());

  /** The PWLS model. */
  private final Pwls pwls_;

  /**
   * Establish the PWLS web service.
   */
  private PwlsWebService()
  {
    logger_.log(Level.INFO, "Initializing PWLS...");

    // Initializing the model
    pwls_ = new Pwls(BASE_URL);

    // Use this for populating based on local files
    //pwls_ = new Pwls(new java.io.File("C:/Users/jacob/dev/pwls/json"));

    logger_.log(Level.INFO, "PWLS is ready.");
  }

  /**
   * Return the local file resource as a string.
   *
   * @param resource  Resource (i.e. file name) to read. Non-null.
   * @return          Content of resource as a string. Never null.
   */
  private static String getText(String resource)
  {
    assert resource != null : "resource cannot be null";

    InputStream stream = PwlsWebService.class.getResourceAsStream(resource);

    StringBuilder s = new StringBuilder();
    try {
      int c = stream.read();
      while (c != -1) {
        s.append((char) c);
        c = stream.read();
      }
    }
    catch (IOException exception) {
        assert false : "Programming error";
    }
    finally {
      try {
        stream.close();
      }
      catch (IOException exception) {
        assert false : "Programming error";
      }
    }

    return s.toString();
  }

  /**
   * @api {get} /properties/ Return all properties
   * @apiName Properties
   * @apiGroup Properties
   * @apiVersion 1.0.0
   *
   * @apiBody {String} [name]  Optional name of property to get.
   *
   * @apiSuccessExample Example response:
   *     {
   *       "firstname": "John",
   *       "lastname": "Doe"
   *     }
   */
  /**
   * Return properties from the PWLS model based on the specified query.
   *
   * @param query  Query to consider. Non-null.
   * @return       The requested properties as a JSON string. Never null.
   */
  private String getProperties(Query query)
  {
    assert query != null : "query cannot be null";

    String name = query.getString("name");
    String quantity = query.getString("quantity");
    Set<Property> properties = pwls_.getProperties(name, quantity);
    return JsonWriter.toString(JsonWriter.getProperties(properties).build());
  }

  /**
   * @api {get} /companies/ Return companies from the PWLS model.
   * @apiName Companies
   * @apiGroup Companies
   * @apiVersion 1.0.0
   *
   * @apiBody {Number} [companyCode]  Optional company code of company to get.
   *
   * @apiSuccessExample Example response:
   *     {
   *       "firstname": "John",
   *       "lastname": "Doe"
   *     }
   */
  /**
   * Return companies from the PWLS model based on the specified query.
   *
   * @param query  Query to consider. Non-null.
   * @return       The requested companies as a JSON string. Never null.
   */
  private String getCompanies(Query query)
  {
    assert query != null : "query cannot be null";

    Integer companyCode = query.getInteger("companyCode");
    Set<Company> companies = pwls_.getCompanies(companyCode);
    return JsonWriter.toString(JsonWriter.getCompanies(companies).build());
  }

  /**
   * Return logging methods from the PWLS model based on the specified query.
   *
   * @param query  Query to consider. Non-null.
   * @return       The requested logging methods as a JSON string. Never null.
   */
  private String getLoggingMethods(Query query)
  {
    assert query != null : "query cannot be null";

    String name = query.getString("name");
    Set<LoggingMethod> loggingMethods = pwls_.getLoggingMethods(name);
    return JsonWriter.toString(JsonWriter.getLoggingMethods(loggingMethods).build());
  }

  /**
   * Return tool classes from the PWLS model based on the specified query.
   *
   * @param query  Query to consider. Non-null.
   * @return       The requested tool classes as a JSON string. Never null.
   */
  private String getToolClasses(Query query)
  {
    assert query != null : "query cannot be null";

    String name = query.getString("name");
    Set<ToolClass> toolClasses = pwls_.getToolClasses(name);
    return JsonWriter.toString(JsonWriter.getToolClasses(toolClasses).build());
  }

  /**
   * Return tools from the PWLS model based on the specified query.
   *
   * @param query  Query to consider. Non-null.
   * @return       The requested tools as a JSON string. Never null.
   */
  private String getTools(Query query)
  {
    assert query != null : "query cannot be null";

    String toolCode = query.getString("toolCode");
    Integer companyCode = query.getInteger("companyCode");
    String group = query.getString("group");
    String genericType = query.getString("genericType");
    String loggingMethod = query.getString("loggingMethod");

    Set<Tool> tools = pwls_.getTools(toolCode, companyCode, group, genericType, loggingMethod);
    return JsonWriter.toString(JsonWriter.getTools(tools).build());
  }

  /**
   * Return curves from the PWLS model based on the specified query.
   *
   * @param query  Query to consider. Non-null.
   * @return       The requested curves as a JSON string. Never null.
   */
  private String getCurves(Query query)
  {
    assert query != null : "query cannot be null";

    String mnemonic = query.getString("mnemonic");
    Integer companyCode = query.getInteger("companyCode");
    String property = query.getString("property");
    String quantity = query.getString("quantity");

    Set<Curve> curves = pwls_.getCurves(mnemonic, companyCode, property, quantity);
    return JsonWriter.toString(JsonWriter.getCurves(curves).build());
  }

  /** {@inheritDoc} */
  @Override
  public void handle(HttpExchange httpExchange)
  {
    String path = httpExchange.getHttpContext().getPath();
    String requestMethod = httpExchange.getRequestMethod();
    String queryString = httpExchange.getRequestURI().getQuery();

    Query query = new Query(queryString);

    logger_.log(Level.INFO, "HTTP Request: " + requestMethod + " " + path + " " + queryString);

    String response = "{}";

    String contentType = "application/json";

    if (path.equals("/") || path.equals("")) {
      response = getText("pwls.html");
      contentType = "text/html";
    }
    if (path.equals("/properties")) {
      response = getProperties(query);
    }
    if (path.equals("/companies")) {
      response = getCompanies(query);
    }
    if (path.equals("/loggingMethods")) {
      response = getLoggingMethods(query);
    }
    if (path.equals("/toolClasses")) {
      response = getToolClasses(query);
    }
    if (path.equals("/curves")) {
      response = getCurves(query);
    }
    if (path.equals("/tools")) {
      response = getTools(query);
    }

    httpExchange.getResponseHeaders().set("Content-Type", contentType);

    try {
      httpExchange.sendResponseHeaders(200, response.getBytes().length);
      OutputStream outputStream = httpExchange.getResponseBody();
      outputStream.write(response.getBytes());
      outputStream.flush();
      httpExchange.close();
    }
    catch (IOException exception) {
      logger_.log(Level.WARNING, "Unable to process request", exception);
    }
  }

  /**
   * Main program for the PWLS web service.
   *
   * @param arguments  Application arguments. Not used.
   */
  public static void main(String[] arguments)
  {
    PwlsWebService pwlsWebService = new PwlsWebService();

    int serverPort = 8081;

    try {
      InetSocketAddress socketAddress = new InetSocketAddress(serverPort);
      HttpServer httpServer = HttpServer.create(socketAddress, 0);
      httpServer.createContext("/", pwlsWebService);
      httpServer.createContext("/properties", pwlsWebService);
      httpServer.createContext("/companies", pwlsWebService);
      httpServer.createContext("/loggingMethods", pwlsWebService);
      httpServer.createContext("/toolClasses", pwlsWebService);
      httpServer.createContext("/tools", pwlsWebService);
      httpServer.createContext("/curves", pwlsWebService);
      httpServer.setExecutor(null);
      httpServer.start();
    }
    catch (IOException exception) {
      logger_.log(Level.WARNING, "Unable to start PWLS web service", exception);
    }
  }
}
