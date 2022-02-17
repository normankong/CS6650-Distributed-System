package cs6650.client;

import cs6650.client.model.ClientConfig;
import cs6650.util.ReportUtility;

/**
 * Skier Client.
 */
public class SkierClient {

  private final ClientConfig config;

  public SkierClient(ClientConfig config) {
    this.config = config;
  }

  /**
   * Driver Program.
   */
  public static void main(String[] argv) throws Exception {
//    if (argv.length != 5) {
//      System.err.println("Thread Skier# Ski# MeanSkiLift URL");
//      System.exit(-1);
//    }

//    int threadNum = Integer.parseInt(argv[0]);
//    int skierNum = Integer.parseInt(argv[1]);
//    int skierLifts = Integer.parseInt(argv[2]);
//    int numRun = Integer.parseInt(argv[3]);
//    String url = argv[4];

    int threadNum = 512;
    int skierNum = 10000;
    int skierLifts = 40;
    int numRun = 20;
    String url = "http://34.222.59.53:8080";
    ClientConfig config = new ClientConfig(threadNum, skierNum, skierLifts, numRun, url);

    System.out.printf("Parameter : [Thread %d][NumRun %d]\n", threadNum, numRun);
    SkierClient client = new SkierClient(config);
    client.execute();
  }

  /**
   * Main Execution logic.
   * Create 3 Phase request runner, then execute 1 by 1,
   * In the RequestRunner, there is 2 CountDownLatch, for Entire / Partial Thread.
   */
  private void execute() throws Exception {
    RequestRunner phase1 = getPhaseRunner(1);
    RequestRunner phase2 = getPhaseRunner(2);
    RequestRunner phase3 = getPhaseRunner(3);

    long totalBefore = ReportUtility.getTime();
    phase1.execute();
    phase2.execute();
    phase3.execute();
    System.out.println("Main thread finish");

    phase1.await();
    phase2.await();
    phase3.await();
    System.out.println("All threads finish");
    long totalAfter = ReportUtility.getTime();

    int executionCount = phase1.getExecutionCount() + phase2.getExecutionCount() + phase3.getExecutionCount();
    double tps = executionCount * Math.pow(10, 3) / (totalAfter - totalBefore);
    System.out.printf("Entire %d / %d (ms) = %f tps\n", executionCount, totalAfter - totalBefore, tps);
  }

  /**
   * Factory method to create RequestRunner.
   */
  private RequestRunner getPhaseRunner(int phase) {
    int numThreads, numRun, startTime, endTime;
    switch (phase) {
      case 1:
        numThreads = (int) Math.ceil(config.getNumThreads() / 4.0);
        numRun = (int) (Math.ceil(config.getNumRun() * 0.2) * config.getNumSkiers() / numThreads);
        startTime = 1;
        endTime = 90;
        break;
      case 2:
        numThreads = (int) Math.ceil(config.getNumThreads());
        numRun = (int) (Math.ceil(config.getNumRun() * 0.6) * config.getNumSkiers() / numThreads);
        startTime = 91;
        endTime = 360;
        break;
      case 3:
        numThreads = (int) Math.ceil(config.getNumThreads() / 10.0);
        numRun = (int) (Math.ceil(config.getNumRun() * 0.1) * config.getNumSkiers() / numThreads);
        startTime = 361;
        endTime = 420;
        break;
      default:
        throw new IllegalArgumentException("Unsupported Phase");
    }

    int numSkiers = config.getNumSkiers();
    int numLifts = config.getNumLifts();
    String url = config.getUrl();

    return new RequestRunner("Phase" + String.valueOf(phase), numThreads, numSkiers, numRun, numLifts, startTime, endTime, url);
  }

}
