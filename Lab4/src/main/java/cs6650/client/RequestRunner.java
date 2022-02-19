package cs6650.client;

import com.google.gson.Gson;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import cs6650.model.LiftRide;
import cs6650.util.ReportUtility;

public class RequestRunner {

  private final int numSkiers;
  private final int numThread;
  private final int numRun;
  private final int numLifts;
  private final int startTime;
  private final int endTime;
  private final String url;
  private final String desc;
  private final CountDownLatch totalCountDownLatch;

  public RequestRunner(String desc, int numThreads, int numSkiers, int numRun, int numLifts, int startTime, int endTime, String url) {
    this.desc = desc;
    this.numSkiers = numSkiers;
    this.numThread = numThreads;
    this.numRun = numRun;
    this.numLifts = numLifts;
    this.startTime = startTime;
    this.endTime = endTime;
    this.url = url;
    this.totalCountDownLatch = new CountDownLatch(this.numThread);

    System.out.printf("Creating %s with thread# %d, numRun %d\n", desc, numThreads, numRun);
  }

  /**
   * Return the total number of execution for this phase.
   */
  public int getExecutionCount() {
    return numThread * numRun;
  }

  /**
   * Main Execution Loop.
   */
  public void execute() throws InterruptedException {

    int partialCount = (int) Math.ceil(this.numThread * 0.2);
    CountDownLatch partialCountDownLatch = new CountDownLatch(partialCount);
    System.out.printf("%s is executing with %d/%d\n", desc, partialCount, numThread);

    List<ThreadRunner> list = new Vector<>();
    for (int i = 0; i < numThread; i++) {
      int startSkierId = 0;//1 + (i * (numSkiers / (numThread)));
      int endSkierId = (i + 1) * (numSkiers / (numThread));
      int skierId = startSkierId + i;//ReportUtility.getRandomInt(startSkierId, endSkierId);
      int time = ReportUtility.getRandomInt(startTime, endTime);
      list.add(new ThreadRunner(desc, skierId, time, numRun, numLifts, url, partialCountDownLatch, totalCountDownLatch));
    }

    long totalBefore = ReportUtility.getTime();
    list.parallelStream().forEach(Thread::start);
    partialCountDownLatch.await();

    System.out.printf("%s have executed %d/%d\n", desc, partialCount, numThread);

    // Start a local thread to process remaining data
    Runnable runnable = new Runnable() {
      @Override
      public void run() {

        try {
          System.out.printf("%s will monitor the remaining threads (%d-%d)\n", desc, partialCount + 1, numThread);

          // Wait for the total Count Down
          totalCountDownLatch.await();

          long totalAfter = ReportUtility.getTime();
          double tps = numThread * numRun * Math.pow(10, 3) / (totalAfter - totalBefore);
          System.out.printf("%s -> %d / %d (ms) = %f tps\n", desc, numThread * numRun, totalAfter - totalBefore, tps);

          ReportUtility.flush(desc, desc + ".csv");
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    };
    new Thread(runnable).start();
  }

  /**
   * Handler to wait for the entire thread completion.
   *
   * @throws InterruptedException
   */
  public void await() throws InterruptedException {
    totalCountDownLatch.await();
  }

  /**
   * Thread Runner to execute the actual work.
   */
  private class ThreadRunner extends Thread {

    private static final String URL_PARAM = "/skiers/999/seasons/20/days/1/skiers/%SKIER_ID%";
    private final Gson gson = new Gson();

    private final String desc;
    private final int skierId;
    private final int time;
    private final int numRun;
    private final int numLifts;
    private final String url;
    private final CountDownLatch partialCountDownLatch;
    private final CountDownLatch totalCountDownLatch;
    private HttpClient httpClient = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .connectTimeout(Duration.ofSeconds(1000))
        .build();

    private ThreadRunner(String desc, int skierId, int time, int numRun, int numLifts, String url, CountDownLatch partialCountDownLatch, CountDownLatch totalCountDownLatch) {
      this.desc = desc;
      this.skierId = skierId;
      this.time = time;
      this.numRun = numRun;
      this.numLifts = numLifts;
      this.url = url;
      this.partialCountDownLatch = partialCountDownLatch;
      this.totalCountDownLatch = totalCountDownLatch;
    }

    @Override
    public void run() {
      String requestBody = createRequestBody();
      String finalURL = this.url + URL_PARAM.replace("%SKIER_ID%", String.valueOf(skierId));

      HttpRequest request = HttpRequest.newBuilder()
          .POST(HttpRequest.BodyPublishers.ofString(requestBody))
          .uri(URI.create(finalURL))
          .setHeader("User-Agent", "Java 11 HttpClient Bot")
          .header("Content-Type", "application/json")
          .timeout(Duration.ofSeconds(1000))
          .build();

      for (int i = 0; i < numRun; i++) {
        long before = ReportUtility.getTime();
        String beforeHHMM = ReportUtility.convertTimeHHMMSS(before);
        HttpResponse<String> response = getStringHttpResponse(request);
        long after = ReportUtility.getTime();
        String afterHHMM = ReportUtility.convertTimeHHMMSS(after);
        if (response != null) {
          ReportUtility.append(desc, String.format("%s,%s,%d,%d,%d,%d,%s,%s,%s", new Date(), "GET", (after - before), response.statusCode(), before, after, beforeHHMM, afterHHMM, skierId));
        } else {
          System.err.printf("%s %d is failed with null response\n", desc, skierId);
        }
      }

      partialCountDownLatch.countDown();
      totalCountDownLatch.countDown();

//      System.out.printf("%s Count %d\n", desc, this.totalCountDownLatch.getCount());
    }

    private HttpResponse<String> getStringHttpResponse(HttpRequest request) {
      for (int i = 0; i < 5; i++) {
        try {
          HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
          if (response.statusCode() != 201) {
            System.err.printf(String.format("%s %d failed with response code : %s\n", desc, skierId, response.statusCode()));
          }
          return response;
        } catch (Exception e) {
          System.err.printf(String.format("%s %d failed with %s\n", desc, skierId, e.getMessage()));
        }
      }
      return null;
    }

    private String createRequestBody() {
      int liftId = ReportUtility.getRandomInt(1, numLifts);
      int waitTime = ReportUtility.getRandomInt(0, 10);
      LiftRide liftRide = new LiftRide();
      liftRide.setLiftID(liftId);
      liftRide.setTime(time);
      liftRide.setWaitTime(waitTime);
      return gson.toJson(liftRide);
    }
  }
}