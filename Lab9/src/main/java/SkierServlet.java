import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import cs6650.jdbc.LiftRideDao;
import cs6650.jdbc.SkiersDao;
import cs6650.model.LiftRide;
import cs6650.model.SkierVertical;
import cs6650.model.SkierVerticalResorts;
import cs6650.util.ChannelObjectFactory;
import cs6650.util.QueueUtility;
import cs6650.util.ServletUtility;
import org.apache.commons.lang3.concurrent.EventCountCircuitBreaker;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

@WebServlet(name = "SkierServlet", value = "/SkierServlet")
public class SkierServlet extends HttpServlet {

  private ObjectPool<Channel> pool;
  private ConnectionFactory factory;

  private EventCountCircuitBreaker faultBreaker;
  private EventCountCircuitBreaker rateBreaker;

  private static final int RATE_LIMIT_HARD_LIMIT = Integer.parseInt(System.getProperty("RATE_LIMIT_HARD_LIMIT"));
  private static final int RATE_LIMIT_SOFT_LIMIT = Integer.parseInt(System.getProperty("RATE_LIMIT_SOFT_LIMIT"));
  private static final int RATE_LIMIT_RATE_PERIOD = Integer.parseInt(System.getProperty("RATE_LIMIT_RATE_PERIOD"));

  private static final int FAULT_OPEN_THRESHOLD = Integer.parseInt(System.getProperty("FAULT_OPEN_THRESHOLD"));
  private static final int FAULT_CLOSE_THRESHOLD = Integer.parseInt(System.getProperty("FAULT_CLOSE_THRESHOLD"));
  private static final int FAULT_OPEN_PERIOD = Integer.parseInt(System.getProperty("FAULT_OPEN_PERIOD"));
  private static final int FAULT_CLOSE_PERIOD = Integer.parseInt(System.getProperty("FAULT_CLOSE_PERIOD"));

  @Override
  public void init() throws ServletException {
    super.init();

    System.out.println("Init Skier Servlet");

    // Init Circuit Breaker
    faultBreaker = new EventCountCircuitBreaker(FAULT_OPEN_THRESHOLD, FAULT_OPEN_PERIOD, TimeUnit.SECONDS, FAULT_CLOSE_THRESHOLD, FAULT_CLOSE_PERIOD, TimeUnit.SECONDS);
    rateBreaker = new EventCountCircuitBreaker(RATE_LIMIT_HARD_LIMIT, RATE_LIMIT_RATE_PERIOD, TimeUnit.SECONDS, RATE_LIMIT_SOFT_LIMIT);

    // Init Rabbit MQ Object Pool
    try {
      factory = new ConnectionFactory();
      factory.setHost(QueueUtility.RABBIT_SERVER);
      factory.setUsername(QueueUtility.RABBIT_USERNAME);
      factory.setPassword(QueueUtility.RABBIT_PASSWORD);
      factory.setConnectionTimeout(5000);

      initConnectionPool();
    } catch (IOException | TimeoutException e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse res) 
    throws ServletException, IOException {

    // Rate Breaker
    if (!rateBreaker.incrementAndCheckState()) {
      res.setContentType("text/plain");
      res.setStatus(429);
      res.addHeader("Retry-After", "1");
      res.getWriter().write("Rate Limit Exceeds, retry after 1s");
      return;
    }

    super.service(req, res);
  }

  private void initConnectionPool() throws IOException, TimeoutException {
    if (pool == null) {
      Connection connection = factory.newConnection();
      pool = new GenericObjectPool<>(new ChannelObjectFactory(connection));
    }
  }

  protected void doPost(HttpServletRequest req, HttpServletResponse res)
      throws IOException {

    String pattern = "^/([0-9]*)/seasons/([0-9]*)/days/([0-9]*)/skiers/([0-9]*)$";
    Pattern p = Pattern.compile(pattern);
    Matcher m = p.matcher(req.getPathInfo());
    if (!m.find()) {
      res.setContentType("text/plain");
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
      res.getWriter().write("missing parameters");
      return;
    }

    Integer resortID = Integer.parseInt(m.group(1));
    String seasonID = m.group(2);
    String dayID = m.group(3);
    Integer skierID = Integer.parseInt(m.group(4));

    Gson gson = new Gson();
    String json = ServletUtility.getRequestBody(req);
    LiftRide liftRide = gson.fromJson(json, LiftRide.class).resortId(resortID).seasonId(seasonID).dayId(dayID).skierId(skierID);
    String jsonText = gson.toJson(liftRide);

    System.out.printf("Incoming request : %s\n", jsonText);

    // Fault Breaker
    if (faultBreaker.checkState()) {
      try {
        initConnectionPool();
        Channel channel = pool.borrowObject();
        channel.basicPublish(QueueUtility.EXCHANGE_NAME, "", null, jsonText.getBytes(StandardCharsets.UTF_8));
        pool.returnObject(channel);
        res.setContentType(ServletUtility.APPLICATION_JSON);
        res.setStatus(HttpServletResponse.SC_CREATED);

      } catch (Exception ex) {
        faultBreaker.incrementAndCheckState();
        res.setContentType(ServletUtility.APPLICATION_JSON);
        res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        res.getWriter().write(ex.getMessage());
      }
    } else {
      res.setContentType(ServletUtility.APPLICATION_JSON);
      res.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
      res.addHeader("Retry-After", "1");
      res.getWriter().write("Fault circuit is open");
    }
  }

  protected void doGet(HttpServletRequest req, HttpServletResponse res)
      throws IOException {

    if (getVerticalBySkierDay(req, res)) {
      return;
    }

    if (getTotalVertical(req, res)) {
      return;
    }

    res.setContentType("text/plain");
    res.setStatus(HttpServletResponse.SC_NOT_FOUND);
    res.getWriter().write("Missing parameters");

  }

  private boolean getVerticalBySkierDay(HttpServletRequest req, HttpServletResponse res) throws IOException {

    String pattern = "^/([0-9]*)/seasons/([0-9]*)/days/([0-9]*)/skiers/([0-9]*)$";
    Pattern p = Pattern.compile(pattern);
    Matcher m = p.matcher(req.getPathInfo());
    if (!m.find()) {
      return false;
    }

    Integer resortID = Integer.parseInt(m.group(1));
    String seasonID = m.group(2);
    String dayID = m.group(3);
    Integer skierID = Integer.parseInt(m.group(4));

    // DAO
    SkiersDao skiersDao = new SkiersDao();
    int height = skiersDao.getHeightByResortIdAndSeasonIdAndDayIdAndSkierId(resortID, seasonID, dayID, skierID);

    res.setContentType(ServletUtility.APPLICATION_JSON);
    res.setStatus(HttpServletResponse.SC_OK);
    res.getWriter().write(String.valueOf(height));

    return true;
  }

  private boolean getTotalVertical(HttpServletRequest req, HttpServletResponse res) throws IOException {

    String pattern = "^/([0-9]*)/vertical$";
    Pattern p = Pattern.compile(pattern);
    Matcher m = p.matcher(req.getPathInfo());
    if (!m.find()) {
      return false;
    }

    Integer skierID = Integer.parseInt(m.group(1));

    // DAO
    SkiersDao skiersDao = new SkiersDao();
    SkierVertical skierVertical = skiersDao.getHeightBySkierId(skierID);

    res.setContentType(ServletUtility.APPLICATION_JSON);
    res.setStatus(HttpServletResponse.SC_OK);
    res.getWriter().write(ServletUtility.toJSON(skierVertical));

    return true;
  }
}
