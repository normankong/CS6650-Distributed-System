package socket;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import socket.handler.ActiveCount;
import socket.handler.SocketHandlerRunnable;

/** * * @author igortn */

public class SocketServer {

  public static void main(String[] args) throws Exception {

    int nThreads = args.length == 1 ? Integer.parseInt(args[0]) : 20;

    // create socket listener
    try (ServerSocket listener = new ServerSocket(12031)){
      // create object to count active threads
      ActiveCount threadCount = new ActiveCount();
      System.out.println("Server started .....");

      // create thread pool and accept connections

      System.out.printf("Number of Server Threads : %d\n", nThreads);
      Executor pool = Executors.newFixedThreadPool(nThreads);

      while (true) {
        Socket conn = listener.accept();
        pool.execute(new SocketHandlerRunnable(conn, threadCount));
      }
    }
  }
}