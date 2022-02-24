package rabbit.callback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

public class RabbitServerCallback implements DeliverCallback {
  private final Channel channel;
  private final Object monitor;

  public RabbitServerCallback(Channel channel, Object monitor) {
    this.channel = channel;
    this.monitor = monitor;
  }

  private static int fib(int n) {
    if (n == 0) return 0;
    if (n == 1) return 1;
    return fib(n - 1) + fib(n - 2);
  }

  @Override
  public void handle(String consumerTag, Delivery delivery) throws IOException {
    AMQP.BasicProperties replyProps = new AMQP.BasicProperties
        .Builder()
        .correlationId(delivery.getProperties().getCorrelationId())
        .build();

    String response = "";

    try {
      String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
      int n = Integer.parseInt(message);

      System.out.println(" [.] fib(" + message + ")");
      response += fib(n);
    } catch (RuntimeException e) {
      System.out.println(" [.] " + e);
    } finally {
      channel.basicPublish("", delivery.getProperties().getReplyTo(), replyProps, response.getBytes(StandardCharsets.UTF_8));
      channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
      // RabbitMq consumer worker thread notifies the RPC server owner thread
      synchronized (monitor) {
        monitor.notify();
      }
    }
  }
}
