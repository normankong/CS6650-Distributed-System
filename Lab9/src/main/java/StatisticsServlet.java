import com.google.gson.Gson;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import cs6650.model.APIStats;
import cs6650.model.APIStatsEndpointStats;

@WebServlet(name = "StatisticsServlet", value = "/StatisticsServlet")
public class StatisticsServlet extends HttpServlet {

  protected void doGet(HttpServletRequest req, HttpServletResponse res)
      throws IOException {

    APIStats apiStats = new APIStats();
    APIStatsEndpointStats apiStatsEndpointStats = new APIStatsEndpointStats();
    apiStatsEndpointStats.setMax(999);
    apiStatsEndpointStats.setMax(001);
    apiStatsEndpointStats.setOperation("GET");
    apiStatsEndpointStats.setURL("/dummy");
    apiStats.addEndpointStatsItem(apiStatsEndpointStats);

    Gson gson = new Gson();
    String response = gson.toJson(apiStats);

    res.setContentType("application/json");
    res.setStatus(HttpServletResponse.SC_OK);
    res.getWriter().write(response);
  }

}
