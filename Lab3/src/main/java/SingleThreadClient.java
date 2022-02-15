import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Date;
import edu.neu.cs6650.util.ReportUtility;

public class SingleThreadClient {

  private static final HttpClient httpClient = HttpClient.newBuilder()
      .version(HttpClient.Version.HTTP_2)
      .connectTimeout(Duration.ofSeconds(10))
      .build();

  public static void main(String[] argv) {
    int loopCount = 10000;
    String url = "http://localhost:8080/lab3/Skiers/12/seasons/2019/days/1/skiers/123";

    SingleThreadClient singleton = new SingleThreadClient();

    System.out.println("Warmup Execution");
    singleton.execute(loopCount, url);

    System.out.println("Actual Execution");
    singleton.execute(loopCount, url);

    System.out.println("Program exits");
  }

  public void execute(int loopCount, String url) {

    System.out.printf("Execute with %d count\n", loopCount);

    long totalBefore = ReportUtility.getTime();

    for (int i = 0; i < loopCount; i++) {

      HttpRequest request = HttpRequest.newBuilder()
          .GET()
          .uri(URI.create(url))
          .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
          .build();

      try {
        long before = ReportUtility.getTime();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        long after = ReportUtility.getTime();
        ReportUtility.append(String.format("%s,%s,%d,%d,%d,%d", new Date(), "GET", (after - before), response.statusCode(), before, after));

      } catch (Exception e) {
        ReportUtility.append(String.format("%d failed with %s", i, e.getMessage()));
      }
    }

    long totalAfter = ReportUtility.getTime();
    double tps = loopCount * Math.pow(10, 3) / (totalAfter - totalBefore) ;
    System.out.printf("%d / %d (ms) = %f tps\n", loopCount, totalAfter - totalBefore, tps );

    ReportUtility.flush("single.result.csv");
  }

}
