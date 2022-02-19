package cs6650.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

/**
 * Utility class for Metrics.
 */
public class MetricsUtility {

  private static final SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
  private static Random random = new Random();
  private static Map<String, List> hash = new Hashtable<>();

  static {
    hash.put("Phase1", new Vector());
    hash.put("Phase2", new Vector());
    hash.put("Phase3", new Vector());
  }

  /**
   * Append message to the buffer.
   */
  public static void append(String phase, String message) {
    List list = hash.get(phase);
    list.add(message);
  }

  /**
   * Flush the current buffer into text file.
   */
  public static void flush(String phase, String filename) {
    List list = hash.get(phase);
    StringBuffer buffer = new StringBuffer();
    list.forEach(x -> buffer.append(x).append("\n"));

    try {
      File file = new File(filename);
      if (!file.exists()) {
        file.createNewFile();
      } else {
        file.delete();
      }
      FileWriter fw = new FileWriter(file.getAbsoluteFile());
      BufferedWriter bw = new BufferedWriter(fw);
      bw.write(buffer.toString());
      bw.close();

      System.out.printf("%d lines have been written to %s\n", list.size(), filename);
//      buffer.setLength(0);
      list.clear();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Return the Current Time in milliseconds.
   */
  public static long getTime() {
    return System.currentTimeMillis();
  }

  /**
   * Return the HHMM of the given time.
   */
  public static String convertTimeHHMMSS(long time) {
    return sdf.format(new Date(time));
  }

  /**
   * Inclusive Random number generator.
   */
  public static int getRandomInt(int lowerBound, int upperBound) {
    return random.nextInt(upperBound - lowerBound + 1) + lowerBound;
  }


}
