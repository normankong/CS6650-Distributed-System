package edu.neu.cs6650.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ReportUtility {

  private static StringBuffer buffer = new StringBuffer();

  public synchronized static void append(String message) {
    buffer.append(message).append("\n");
  }

  public static void flush(String filename) {
    try {
      File file = new File(filename);
      if (!file.exists()) {
        file.createNewFile();
      }else{
        file.delete();
      }
      FileWriter fw = new FileWriter(file.getAbsoluteFile());
      BufferedWriter bw = new BufferedWriter(fw);
      bw.write(buffer.toString());
      bw.close();

      System.out.printf("%d lines have been written to %s\n", buffer.toString().split("\n").length, filename);
      buffer.setLength(0);


    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static long getTime() {
//    return System.nanoTime();
    return System.currentTimeMillis();
  }
}
