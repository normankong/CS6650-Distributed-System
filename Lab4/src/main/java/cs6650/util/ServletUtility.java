package cs6650.util;

import com.google.gson.Gson;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class ServletUtility {

  public static final String APPLICATION_JSON = "application/json";

  public static String toJSON(Object object) {
    Gson gson = new Gson();
    return gson.toJson(object);
  }

  public static String getRequestBody(HttpServletRequest req) throws IOException {
    String json = req.getReader().lines()
        .reduce("", (accumulator, actual) -> accumulator + actual);
    return json;
  }
}
