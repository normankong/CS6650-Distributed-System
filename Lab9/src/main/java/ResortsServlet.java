import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import cs6650.jdbc.ResortsDao;
import cs6650.model.ResortsList;
import cs6650.model.ResortsListResorts;
import cs6650.model.SeasonsList;
import cs6650.model.UniqueSkier;
import cs6650.util.ServletUtility;

@WebServlet(name = "ResortsServlet", value = "/ResortsServlet")
public class ResortsServlet extends HttpServlet {

  /**
   * GET  /resorts                                                   get a list of ski resorts in the database
   * GET  /resorts/{resortID}/seasons/{seasonID}/day/{dayID}/skiers  get number of unique skiers at resort/season/day
   * GET  /resorts/{resortID}/seasons                                get a list of seasons for the specified resort
   * POST /resorts/{resortID}/seasons                                Add a new season for a resort
   **/
  private static boolean isMatch(String urlPath, String pattern) {
    List<String> list = new Vector<>();
    list.add(pattern);
    return isUrlValid(urlPath, list);
  }

  private static boolean isUrlValid(String urlPath, List<String> list) {
    boolean isMatch = list.stream().anyMatch(x -> {
      Pattern p = Pattern.compile(x);
      Matcher m = p.matcher(urlPath);
      return m.matches();
    });

    return isMatch;
  }

  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {

    String pattern = "^/([0-9]*)/seasons$";
    String urlPath = req.getPathInfo();

    Pattern p = Pattern.compile(pattern);
    Matcher m = p.matcher(urlPath);
    if (!m.find()) {
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
      res.getWriter().write("Invalid parameters");
      return;
    }

    String resortID = m.group(1);
    res.setContentType(ServletUtility.APPLICATION_JSON);
    res.setStatus(HttpServletResponse.SC_CREATED);
  }

  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {

    if (getResorts(req, res)) {
      return;
    }

    if (getUniqueSkiers(req, res)) {
      return;
    }

    if (getSeasonsList(req, res)) {
      return;
    }

    // Send Default Message
    res.setStatus(HttpServletResponse.SC_NOT_FOUND);
    res.getWriter().write("Invalid parameters");
  }

  private boolean getResorts(HttpServletRequest req, HttpServletResponse res) throws IOException {
    String urlPath = req.getPathInfo();
    if (urlPath != null) {
      return false;
    }

    ResortsListResorts item = new ResortsListResorts();
    item.setResortID(1234);
    item.setResortName("Dummy");
    ResortsList list = new ResortsList();
    list.addResortsItem(item);

    res.setContentType(ServletUtility.APPLICATION_JSON);
    res.setStatus(HttpServletResponse.SC_OK);
    res.getWriter().write(ServletUtility.toJSON(list));

    return true;
  }

  private boolean getUniqueSkiers(HttpServletRequest req, HttpServletResponse res) throws IOException {
    String pattern = "^/([0-9]*)/seasons/([0-9]*)/day/([0-9]*)/skiers$";
    String urlPath = req.getPathInfo();

    Pattern p = Pattern.compile(pattern);
    Matcher m = p.matcher(urlPath);
    if (!m.find()) {
      return false;
    }

    Integer resortID = Integer.parseInt(m.group(1));
    String seasonID = m.group(2);
    String dayID = m.group(3);

    ResortsDao dao = new ResortsDao();
    int count = dao.getCountByResortIdAndSeasonIdAndDayId(resortID, seasonID, dayID);
    UniqueSkier uniqueSkier = new UniqueSkier("Mission Ridge", count);

//    Add SQL
//    select count(1) from LiftRids where resortId = ? and seasonId = ? and day = ?;
    res.setContentType(ServletUtility.APPLICATION_JSON);
    res.setStatus(HttpServletResponse.SC_OK);
    res.getWriter().write(ServletUtility.toJSON(uniqueSkier));

    return true;
  }

  private boolean getSeasonsList(HttpServletRequest req, HttpServletResponse res) throws IOException {
    String pattern = "^/([0-9]*)/seasons$";
    String urlPath = req.getPathInfo();

    Pattern p = Pattern.compile(pattern);
    Matcher m = p.matcher(urlPath);
    if (!m.find()) {
      return false;
    }

    String resortID = m.group(1);

    SeasonsList seasonsList = new SeasonsList();
    seasonsList.addSeasonsItem("SUMMER " + resortID);

    res.setContentType(ServletUtility.APPLICATION_JSON);
    res.setStatus(HttpServletResponse.SC_OK);
    res.getWriter().write(ServletUtility.toJSON(seasonsList));
    return true;
  }

}
