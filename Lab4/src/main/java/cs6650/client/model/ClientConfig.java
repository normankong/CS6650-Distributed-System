package cs6650.client.model;

/**
 * Class for the Client Configuration.
 */
public class ClientConfig {

  private int numThreads;
  private int numSkiers;
  private int numLifts;
  private int numRun;
  private String url;

  public ClientConfig(int numThreads, int numSkiers, int numLifts, int numRun, String url) {
    this.numThreads = numThreads;
    this.numSkiers = numSkiers;
    this.numLifts = numLifts;
    this.numRun = numRun;
    this.url = url;
  }

  public int getNumThreads() {
    return numThreads;
  }

  public int getNumSkiers() {
    return numSkiers;
  }

  public int getNumLifts() {
    return numLifts;
  }

  public int getNumRun() {
    return numRun;
  }

  public String getUrl() {
    return url;
  }
}