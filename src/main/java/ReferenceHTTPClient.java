import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ReferenceHTTPClient {

  private static final HttpClient httpClient = HttpClient.newBuilder()
      .version(HttpClient.Version.HTTP_1_1)
      .connectTimeout(Duration.ofSeconds(10))
      .build();

  private static final ExecutorService executorService = Executors.newFixedThreadPool(5);

  private static final HttpClient httpMultiClient = HttpClient.newBuilder()
      .executor(executorService)
      .version(HttpClient.Version.HTTP_2)
      .connectTimeout(Duration.ofSeconds(10))
      .build();

  public static void main(String[] argv) throws Exception {

    String url = "http://localhost:8080/lab3/Skiers/12/seasons/2019/days/1/skiers/123";
//    runSynchronizeGet(url);
//    runAsynchronousGet(url);

    int threadCount = 3;
    runMultipleThreadGet(url, threadCount);

    // Shutdown the ExecutorService
    executorService.shutdownNow();
  }

  private static void runMultipleThreadGet(String link, int threadCount) throws Exception {
    URI uri = new URI(link);
    List<URI> targets = new ArrayList<>();
    for (int i = 0; i < threadCount; i++) {
      targets.add(uri);
    }

    List<CompletableFuture<String>> result = targets.stream()
        .map(url -> httpMultiClient.sendAsync(
                HttpRequest.newBuilder(url)
                    .GET()
                    .setHeader("User-Agent", "Java 11 HttpClient Bot")
                    .build(),
                HttpResponse.BodyHandlers.ofString())
            .thenApply(response -> response.body()))
        .collect(Collectors.toList());

    for (CompletableFuture<String> future : result) {
      System.out.println(future.get());
    }
  }

  private static void runSynchronizeGet(String url) throws Exception {
    HttpRequest request = HttpRequest.newBuilder()
        .GET()
        .uri(URI.create(url))
        .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
        .build();

    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    // print response headers
    HttpHeaders headers = response.headers();
    headers.map().forEach((k, v) -> System.out.println(k + ":" + v));

    // print status code
    System.out.println(response.statusCode());

    // print response body
    System.out.println(response.body());
  }

  private static void runAsynchronousGet(String url) throws Exception {
    HttpRequest request = HttpRequest.newBuilder()
        .GET()
        .uri(URI.create(url))
        .setHeader("User-Agent", "Java 11 HttpClient Bot")
        .build();

    long before = System.currentTimeMillis();
    CompletableFuture<HttpResponse<String>> response =
        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

    String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
    long after = System.currentTimeMillis();

    System.out.printf("%d", after-before);
  }
}
