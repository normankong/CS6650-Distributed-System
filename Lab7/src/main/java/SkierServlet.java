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

  private EventCountCircuitBreaker breaker;

  private ConnectionFactory factory;

  @Override
  public void init() throws ServletException {
    super.init();

    System.out.println("Init Skier Servlet");

    // Init Circuit Breaker
    breaker = new EventCountCircuitBreaker(5, 2, TimeUnit.MINUTES, 5, 1, TimeUnit.MINUTES);

    // Init Rabbit MQ Object Pool
    try {
      factory = new ConnectionFactory();
      factory.setHost(QueueUtility.RABBIT_SERVER);
      factory.setUsername(QueueUtility.RABBIT_USERNAME);
      factory.setPassword(QueueUtility.RABBIT_PASSWORD);
      factory.setConnectionTimeout(3000);

      initConnectionPool();
    } catch (IOException | TimeoutException e) {
      e.printStackTrace();
    }
  }

  private void initConnectionPool() throws IOException, TimeoutException {
    if (pool == null){
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

    // Only Trigger RabbitMQ if Resort ID is 999999999
    if (breaker.checkState()) {
      try {
        initConnectionPool();
        Channel channel = pool.borrowObject();
        channel.basicPublish("", QueueUtility.QUEUE_NAME, null, jsonText.getBytes(StandardCharsets.UTF_8));
        pool.returnObject(channel);
        System.out.printf("Connection Pool : %d vs %d\n", pool.getNumActive(), pool.getNumIdle());

        res.setContentType(ServletUtility.APPLICATION_JSON);
        res.setStatus(HttpServletResponse.SC_CREATED);
        return;

      } catch (Exception ex) {
        breaker.incrementAndCheckState();
        ex.printStackTrace();
        res.setContentType(ServletUtility.APPLICATION_JSON);
        res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        res.getWriter().write(ex.getMessage());
        return;
      }
    } else {
      // return an error code, use an alternative service, etc.
      res.setContentType(ServletUtility.APPLICATION_JSON);
      res.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
      res.getWriter().write("Circuit Breaking");
      return;
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

    String resortID = m.group(1);
    String seasonID = m.group(2);
    String dayID = m.group(3);
    String skierID = m.group(4);

    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    res.setContentType(ServletUtility.APPLICATION_JSON);
    res.setStatus(HttpServletResponse.SC_OK);
    res.getWriter().write("1234");

    return true;
  }

  private boolean getTotalVertical(HttpServletRequest req, HttpServletResponse res) throws IOException {

    String pattern = "^/([0-9]*)/vertical$";
    Pattern p = Pattern.compile(pattern);
    Matcher m = p.matcher(req.getPathInfo());
    if (!m.find()) {
      return false;
    }

    String skierID = m.group(1);

    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    SkierVertical skierVertical = new SkierVertical();
    SkierVerticalResorts item = new SkierVerticalResorts();
    item.totalVert(1234).seasonID("9999");
    skierVertical.addResortsItem(item);

    res.setContentType(ServletUtility.APPLICATION_JSON);
    res.setStatus(HttpServletResponse.SC_OK);
    res.getWriter().write(ServletUtility.toJSON(skierVertical));

    return true;
  }
}
