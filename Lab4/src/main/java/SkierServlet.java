import com.google.gson.Gson;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import cs6650.model.LiftRide;
import cs6650.model.SkierVertical;
import cs6650.model.SkierVerticalResorts;
import cs6650.util.ServletUtility;

@WebServlet(name = "SkierServlet", value = "/SkierServlet")
public class SkierServlet extends HttpServlet {

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


    try {
      Thread.sleep(Integer.parseInt(resortID));
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
