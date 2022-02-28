import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import cs6650.model.LiftRide;
import cs6650.model.SkierVertical;
import cs6650.model.SkierVerticalResorts;
import cs6650.util.QueueUtility;
import cs6650.util.ServletUtility;
import org.json.simple.JSONValue;

@WebServlet(name = "SkierServlet", value = "/SkierServlet")
public class SkierServlet extends HttpServlet {

  private ConnectionFactory factory;
  private Channel channel;

  @Override
  public void init() throws ServletException {
    super.init();

    System.out.println("Initialize Connection");
    factory = new ConnectionFactory();
    factory.setHost(QueueUtility.RABBIT_SERVER);
    factory.setUsername(QueueUtility.RABBIT_USERNAME);
    factory.setPassword(QueueUtility.RABBIT_PASSWORD);

    try {
      Connection connection = factory.newConnection();
      channel = connection.createChannel();
      channel.queueDeclare(QueueUtility.QUEUE_NAME, false, false, false, null);
    } catch (IOException | TimeoutException e) {
      e.printStackTrace();
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

    String resortID = m.group(1);
    String seasonID = m.group(2);
    String dayID = m.group(3);
    String skierID = m.group(4);

    Gson gson = new Gson();
    String json = ServletUtility.getRequestBody(req);
    LiftRide liftRide = gson.fromJson(json, LiftRide.class);

    Map obj = new HashMap();
    obj.put("resortID", resortID);
    obj.put("seasonID", seasonID);
    obj.put("salary", dayID);
    obj.put("skierID", skierID);
    obj.put("liftID", liftRide.getLiftID());
    obj.put("time", liftRide.getTime());
    obj.put("waitTime", liftRide.getWaitTime());
    String jsonText = JSONValue.toJSONString(obj);


    // Only Trigger RabbitMQ if Resort ID is 999999999
    if (resortID.equals("999999999")) {
//      try (
//          Connection connection = factory.newConnection();
//          Channel channel = connection.createChannel()) {
//        channel.queueDeclare(QueueUtility.QUEUE_NAME, false, false, false, null);
        channel.basicPublish("", QueueUtility.QUEUE_NAME, null, jsonText.getBytes(StandardCharsets.UTF_8));
//      } catch (IOException | TimeoutException e) {
//        e.printStackTrace();
//      }
    }

    // Sleep Seasons ID ms
    try {
      Thread.sleep(Integer.parseInt(seasonID));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    res.setContentType(ServletUtility.APPLICATION_JSON);
    res.setStatus(HttpServletResponse.SC_CREATED);
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
