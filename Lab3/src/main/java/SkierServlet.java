import com.google.gson.Gson;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import edu.neu.cs6650.model.LiftRide;

@WebServlet(name = "SkierServlet", value = "/SkierServlet")
public class SkierServlet extends HttpServlet {

  protected void doPost(HttpServletRequest req, HttpServletResponse res)
      throws IOException {

//    Gson gson = new Gson();
//    String json = req.getReader().lines()
//        .reduce("", (accumulator, actual) -> accumulator + actual);

//    LiftRide liftRide = gson.fromJson(json, LiftRide.class);

    String urlPath = req.getPathInfo();
    // check we have a URL!
    if (urlPath == null || urlPath.isEmpty()) {
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
      res.getWriter().write("missing parameters");
      return;
    }

    String[] urlParts = urlPath.split("/");
    // and now validate url path and return the response status code
    // (and maybe also some value if input is valid)

    res.setContentType("text/plain");
    if (!isUrlValid(urlParts)) {
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } else {
      res.setStatus(HttpServletResponse.SC_CREATED);
      res.getWriter().write("It works post!");
    }
  }

  protected void doGet(HttpServletRequest req, HttpServletResponse res)
      throws IOException {
    res.setContentType("text/plain");
    String urlPath = req.getPathInfo();

    // check we have a URL!
    if (urlPath == null || urlPath.isEmpty()) {
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
      res.getWriter().write("missing parameters");
      return;
    }

    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    String[] urlParts = urlPath.split("/");
    // and now validate url path and return the response status code
    // (and maybe also some value if input is valid)

    if (!isUrlValid(urlParts)) {
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } else {
      res.setStatus(HttpServletResponse.SC_OK);
      res.getWriter().write("It works!");
    }
  }

  private boolean isUrlValid(String[] urlPath) {
    if ((urlPath.length == 3) && urlPath[2].equals("vertical")) {
      return true;
    }

    return (urlPath.length == 8) &&
        urlPath[2].equals("seasons") &&
        urlPath[4].equals("days") &&
        urlPath[6].equals("skiers");
  }


}
