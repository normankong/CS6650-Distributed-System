package cs6650.util;

import java.io.IOException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.DestroyMode;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class ChannelObjectFactory extends BasePooledObjectFactory<Channel> {

  private Connection connection;
  public ChannelObjectFactory(Connection connection) {
    this.connection = connection;
  }

  @Override
  public Channel create() throws Exception {
    Channel channel = connection.createChannel();

    // Send to Queue
    // channel.queueDeclare(QueueUtility.QUEUE_NAME, false, false, false, null);

    // Send to Exchange
    channel.exchangeDeclare(QueueUtility.EXCHANGE_NAME, "fanout");

    return channel;
  }

  @Override
  public PooledObject<Channel> wrap(Channel channel) {
    return new DefaultPooledObject<Channel>(channel);
  }

  @Override
  public void destroyObject(PooledObject<Channel> p, DestroyMode destroyMode) throws Exception {
    super.destroyObject(p, destroyMode);
  }
}