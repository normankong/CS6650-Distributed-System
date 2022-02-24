package socket.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketClientThread extends Thread {
  private final static int NUM_ITERATIONS = 1000;
  private long clientID;
  private String hostName;
  private int port;
  private CyclicBarrier synk;

  public static Set<Integer> set  = new HashSet();

  public SocketClientThread(String hostName, int port, CyclicBarrier barrier) {
    this.hostName = hostName;
    this.port = port;
    synk = barrier;

  }

  @Override
  public void run() {
    clientID = Thread.currentThread().getId();
    // pass messages to the SocketServer
    Socket s;
    for (int i = 0; i < NUM_ITERATIONS; i++) {
      try {
        s = new Socket(hostName, port);
//        SocketAddress socketAddress = new InetSocketAddress(hostName, port);
//        s = new Socket();
//        s.connect(socketAddress, 999_999);
        PrintWriter out =
            new PrintWriter(s.getOutputStream(), true);
        BufferedReader in =
            new BufferedReader(
                new InputStreamReader(s.getInputStream()));
        out.println("Client ID is " + Long.toString(clientID));
        String line = in.readLine();
        Integer threadId = Integer.parseInt(line.substring(29));
        set.add(threadId);

//        System.out.println(in.readLine());
        System.out.print(".");

      } catch (UnknownHostException e) {
        // if we get an exception, don't bother retrying
        System.out.println("Don't know about host " + hostName);
//        break;
      } catch (IOException e) {
        System.out.println("Couldn't get I/O for the connection to " +
            hostName);
        // if we get an exception, don't bother retrying
//        break;
      }
    }
    try {
      // wait on the CyclicBarrier
      System.out.println("Thread waiting at barrier");
      synk.await();
    } catch (InterruptedException | BrokenBarrierException ex) {
      Logger.getLogger(SocketClientThread.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}

