package cs6650.consumer;

import com.google.gson.Gson;
import cs6650.jdbc.LiftRideDao;
import cs6650.model.LiftRide;

public class SkierProcessor {

  private static final Gson gson = new Gson();

  public static void process(String message) {

    LiftRide liftRide = gson.fromJson(message, LiftRide.class);

    LiftRideDao liftRideDao = new LiftRideDao();
    liftRideDao.createLiftRide(liftRide);

    System.out.printf("Record inserted : %s\n", message);
  }
}
