package adm.gaia.events.indexer;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

/**
 * Created by tsadok on 17/06/2015.
 */
public class EventIndexerConsumer extends DefaultConsumer {

    public EventIndexerConsumer(Channel channel) {
        super(channel);
    }

    public void handleDelivery(String consumerTag, Envelope envelope,
                               AMQP.BasicProperties props, byte[] body)
    {
        System.out.println(" [*] Channel " + getChannel().toString() +
                Thread.currentThread().toString() + "Received: " + body.toString());

        try {
            getChannel().basicAck(envelope.getDeliveryTag(), false);
        } catch (IOException e) {
            //TODO: log it
            e.printStackTrace();
        }
    };
}
