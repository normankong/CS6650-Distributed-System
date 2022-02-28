package cs6650.consumer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class SkierProcessor {

  private static Map<Integer, List<JSONObject>> map = new HashMap();

  public static void process(String message) {

    System.out.println(message);

    JSONObject jsonObject = (JSONObject) JSONValue.parse(message);
    int skierId = Integer.parseInt((String) jsonObject.get("skierID"));
    List list = map.getOrDefault(skierId, new LinkedList<>());
    list.add(jsonObject);
    map.put(skierId, list);
//    System.out.printf("Total Map size : %d\n", map.size());
  }
}
