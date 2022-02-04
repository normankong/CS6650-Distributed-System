import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import edu.neu.cs6650.util.ReportUtility;

public class MultipleThreadClient {

  private static HttpClient httpClient = HttpClient.newBuilder()
      .version(HttpClient.Version.HTTP_2)
      .connectTimeout(Duration.ofSeconds(10))
      .build();

  public static void main(String[] argv) throws InterruptedException {

    int threadCount = (argv.length == 0) ? 10 : Integer.parseInt(argv[0]);
    int loopCount = (argv.length == 0) ? 100 : Integer.parseInt(argv[1]);
    String url = "http://localhost:8080/lab3/Skiers/12/seasons/2019/days/1/skiers/123";

    MultipleThreadClient singleton = new MultipleThreadClient();
    System.out.println("Warmup Execution 1");
    singleton.execute(threadCount, url, loopCount);

    System.out.println("Warmup Execution 2");
    singleton.execute(threadCount, url, loopCount);

    System.out.println("Warmup Execution 3");
    singleton.execute(threadCount, url, loopCount);

    System.out.println("Real Execution");
    singleton.execute(threadCount, url, loopCount);

    System.out.println("Program exits");
  }

  public void execute(int threadCount, String url, int loopCount) throws InterruptedException {

    CountDownLatch countDownLatch = new CountDownLatch(threadCount);
    List<MyThread> list = new ArrayList<>();
    for (int i = 0; i < threadCount; i++) {
      list.add(new MyThread(i, url, loopCount, countDownLatch));
    }

    System.out.printf("Execute with %d threads with loopCount %d\n", threadCount, loopCount);

    long totalBefore = ReportUtility.getTime();
    list.parallelStream().forEach(Thread::start);
    countDownLatch.await();
    long totalAfter = ReportUtility.getTime();

    double tps = threadCount * loopCount * Math.pow(10, 3) / (totalAfter - totalBefore);
    System.out.printf("%d / %d (ms) = %f tps\n", threadCount * loopCount, totalAfter - totalBefore, tps);

    ReportUtility.flush("multiple.result." + threadCount * loopCount + ".csv");
  }

  /**
   * Private Class for MyThread to run.
   */
  private class MyThread extends Thread {
    private final int id;
    private final String url;
    private final int loopCount;
    private final CountDownLatch countDownLatch;

    public MyThread(int id, String url, int loopCount, CountDownLatch countDownLatch) {
      this.countDownLatch = countDownLatch;
      this.id = id;
      this.url = url;
      this.loopCount = loopCount;
    }

    public void run() {
      try {

        HttpRequest request = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create(url))
            .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
            .build();

        for (int i = 0; i < loopCount; i++) {
          long before = ReportUtility.getTime();
          HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
          long after = ReportUtility.getTime();
          ReportUtility.append(String.format("%s,%s,%d,%d,%d,%d", new Date(), "GET", (after - before), response.statusCode(), before, after));
        }
      } catch (Exception e) {
        ReportUtility.append(String.format("%d failed with %s", id, e.getMessage()));
      }

      this.countDownLatch.countDown();
    }
  }
}
