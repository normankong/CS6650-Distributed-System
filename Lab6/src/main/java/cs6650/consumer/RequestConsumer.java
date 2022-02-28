package cs6650.consumer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import com.rabbitmq.client.Envelope;
import cs6650.util.QueueUtility;

public class RequestConsumer {

  private static int THREAD_POOL = 5;

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost(QueueUtility.RABBIT_SERVER);
    factory.setUsername(QueueUtility.RABBIT_USERNAME);
    factory.setPassword(QueueUtility.RABBIT_PASSWORD);

    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    channel.basicQos(10);
    channel.queueDeclare(QueueUtility.QUEUE_NAME, false, false, false, null);
    //    channel.QueueDeclare(queue: "hello", durable: true, exclusive: false, autoDelete: false, arguments: null);

    ExecutorService threadExecutor = Executors.newFixedThreadPool(THREAD_POOL);

    int prefetchCount = 1;
    new Worker(prefetchCount, threadExecutor, connection.createChannel(), QueueUtility.QUEUE_NAME);

    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
  }

  /**
   * Using Default Consumer to handle multiple thread
   */
  static class Worker extends DefaultConsumer {

    private ExecutorService executorService;
    private Channel channel;

    public Worker(int prefetch, ExecutorService threadExecutor,
                  Channel channel, String queue) throws Exception {
      super(channel);

      this.channel = channel;
      this.channel.basicQos(prefetch);
      this.executorService = threadExecutor;

      // this.channel.basicConsume(queue, true, new RabbitDeliverCallback(), new RabbitCancelCallback());
      this.channel.basicConsume(queue, true, this);
    }

    @Override
    public void handleDelivery(String consumerTag,
                               Envelope envelope,
                               AMQP.BasicProperties properties,
                               byte[] body) throws IOException {

      Runnable task = () -> {
        String message = new String(body, StandardCharsets.UTF_8);
        SkierProcessor.process(message);
      };

      executorService.submit(task);
    }
  }

  /**
   * Simple Rabbit Deliver Callback method
   */
  static class RabbitDeliverCallback implements DeliverCallback {
    @Override
    public void handle(String consumerTag, Delivery delivery) throws IOException {
      String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
      SkierProcessor.process(message);
    }
  }

  /**
   * Simple Delivery Cancel Callback
   */
  static class RabbitCancelCallback implements CancelCallback {
    @Override
    public void handle(String deliveryTag) throws IOException {
      System.out.println(deliveryTag);
    }
  }
}
