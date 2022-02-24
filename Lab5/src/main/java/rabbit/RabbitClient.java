package rabbit;

import rabbit.callback.RabbitClientRequest;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitClient  {

  public static void main(String[] argv) {
    try (RabbitClientRequest request = new RabbitClientRequest()) {
      for (int i = 0; i < 32; i++) {
        String i_str = Integer.toString(i);
        System.out.println(" [x] Requesting fib(" + i_str + ")");
        String response = request.call(i_str);
        System.out.println(" [.] Got '" + response + "'");
      }
    } catch (IOException | TimeoutException | InterruptedException e) {
      e.printStackTrace();
    }
  }

}

