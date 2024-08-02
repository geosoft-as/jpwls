package no.geosoft.jpwls;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import no.geosoft.jpwls.json.JsonWriter;

/**
 * The PWLS web service.
 *
 * @author <a href="mailto:jacob.dreyer@geosoft.no">Jacob Dreyer</a>
 */
public final class PwlsWebService
{
  /** Base URL for PWLS JSON files. */
  private static final String BASE_URL = "https://raw.githubusercontent.com/geosoft-as/pwls/main/json";

  /** The logginr instance. */
  private static final Logger logger_ = Logger.getLogger(PwlsWebService.class.getName());

  /** The HTTP request handler instance. */
  private static final HttpRequestHandler httpRequestHandler_ = new HttpRequestHandler();

  /**
   * Private constructor to prevent client instantiation.
   */
  private PwlsWebService()
  {
    assert false : "This constructor should never be called";
  }

  /**
   * The handler of HTTP requests.
   *
   * @author <a href="mailto:jacob.dreyer@geosoft.no">Jacob Dreyer</a>
   */
  private static class HttpRequestHandler implements HttpHandler
  {
    /** The PWLS model. */
    private final Pwls pwls_;

    /**
     * Crete the HTTP request handler.
     */
    private HttpRequestHandler()
    {
      logger_.log(Level.INFO, "Initializing PWLS...");

      // Initializing the model
      pwls_ = new Pwls(BASE_URL);

      // Use this for populating on local files
      // pwls_ = new Pwls(new java.io.File("C:/Users/jacob/dev/pwls/standard"));

      logger_.log(Level.INFO, "PWLS is ready.");
    }

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
      Set<Property> properties = pwls_.getProperties(name);
      return JsonWriter.toString(JsonWriter.getProperties(properties).build());
    }

    /**
     * Return companies from the PWLS model based on the specified query.
     *
     * @param query  Query to consider. Non-null.
     * @return       The requested companies as a JSON string. Never null.
     */
    private String getCompanies(Query query)
    {
      assert query != null : "query cannot be null";

      Integer companyCode = query.getInteger("code");
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

      String code = query.getString("code");
      Integer companyCode = query.getInteger("companyCode");
      String group = query.getString("group");
      String genericType = query.getString("genericType");
      String loggingMethod = query.getString("loggingMethod");

      Set<Tool> tools = pwls_.getTools(code, companyCode, group, genericType, loggingMethod);
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

      Set<Curve> curves = pwls_.getCurves(mnemonic, companyCode);
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

      if (path.equals("/properties")) {
        response = getProperties(query);
      }
      if (path.equals("/companies")) {
        response = getCompanies(query);
      }
      if (path.equals("/loggingmethods")) {
        response = getLoggingMethods(query);
      }
      if (path.equals("/toolclasses")) {
        response = getToolClasses(query);
      }
      if (path.equals("/curves")) {
        response = getCurves(query);
      }
      if (path.equals("/tools")) {
        response = getTools(query);
      }

      httpExchange.getResponseHeaders().set("Content-Type", "application/json");

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
  }

  /**
   * Main program for the PWLS web service.
   *
   * @param arguments  Application arguments. Not used.
   */
  public static void main(String[] arguments)
  {
    int serverPort = 8000;

    try {
      InetSocketAddress socketAddress = new InetSocketAddress(serverPort);
      HttpServer httpServer = HttpServer.create(socketAddress, 0);
      httpServer.createContext("/properties", httpRequestHandler_);
      httpServer.createContext("/companies", httpRequestHandler_);
      httpServer.createContext("/loggingmethods", httpRequestHandler_);
      httpServer.createContext("/toolclasses", httpRequestHandler_);
      httpServer.createContext("/tools", httpRequestHandler_);
      httpServer.createContext("/curves", httpRequestHandler_);
      httpServer.setExecutor(null);
      httpServer.start();
    }
    catch (IOException exception) {
      logger_.log(Level.WARNING, "Unable to start PWLS web service");
    }
  }
}
