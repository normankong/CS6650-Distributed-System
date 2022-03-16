package cs6650.consumer;

import com.google.gson.Gson;
import cs6650.jdbc.LiftRideDao;
import cs6650.model.LiftRide;

public class SkierProcessor {

  private static final Gson gson = new Gson();
  private static int recordCount = 0;

  public static void process(String message) {
    LiftRide liftRide = gson.fromJson(message, LiftRide.class);

    LiftRideDao liftRideDao = new LiftRideDao();
    liftRideDao.createLiftRide(liftRide);

    if (recordCount <= 100) {
      System.out.printf("Record inserted : %s\n", message);
    }
    if (recordCount++ % 100 == 0 && recordCount > 100) {
      System.out.printf("Processing records %d\n", recordCount);
    }
  }
}
