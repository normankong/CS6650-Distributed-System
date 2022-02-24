package socket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import socket.handler.SocketClientThread;
import util.MetricsUtility;

public class SocketClient {

  static CyclicBarrier barrier;

  public static void main(String[] args) throws InterruptedException, BrokenBarrierException {

    String hostName = "localhost";
    int port = 12031;
    int nThreads = args.length == 1 ? Integer.parseInt(args[0]) : 50;

    //initialization of barrier for the threads
    barrier = new CyclicBarrier(nThreads + 1);

    long beforeTime = MetricsUtility.getTime();

    System.out.printf("Start client with %d\n", nThreads);
    //create and start MAX_THREADS SocketClientThread
    for (int i = 0; i < nThreads; i++) {
      new SocketClientThread(hostName, port, barrier).start();
    }

    //wait for all threads to complete
    barrier.await();

    long afterTime = MetricsUtility.getTime();

    System.out.printf("Throughput : %d/%d = %f tps\n", nThreads * 1000, afterTime - beforeTime, (double) nThreads * 1000 * 1000 / (afterTime - beforeTime));

    System.out.println("Terminating ....");

    List list = new ArrayList<>(SocketClientThread.set);
    Collections.sort(list);
//    Iterator iterator = list.iterator();
//    while (iterator.hasNext()) {
//      System.out.println(iterator.next());
//    }

    System.out.println(list.get(list.size()-1));

  }
}

