package adm.gaia.events.indexer;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import javax.ws.rs.client.WebTarget;
import java.io.IOException;

/**
 * Created by tsadok on 17/06/2015.
 */
public class EventIndexerConsumer extends DefaultConsumer {

    InfluxDBManager influxDBManager;

    public EventIndexerConsumer(Channel channel, InfluxDBManager influxDBManager) {
        super(channel);
        this.influxDBManager = influxDBManager;
    }

    public void handleDelivery(String consumerTag, Envelope envelope,
                               AMQP.BasicProperties props, byte[] body)
    {
        System.out.println(" [*] Channel " + getChannel().toString() +
                Thread.currentThread().toString() + "Received: " + body.toString());

        try {
            String dbName = props.getHeaders().get("dbname").toString();
            if (dbName == null)
            {
                System.err.println("Rabbitmq dbname header property was empty, rejecting the message");
                getChannel().basicNack(envelope.getDeliveryTag(), false, false);
            }

            WebTarget webTarget = influxDBManager.getJerseyClient().target(influxDBManager.getInfluxDbBaseUrl() + dbName + "/series" + influxDBManager.getInfluxDbQueryParams());
            getChannel().basicAck(envelope.getDeliveryTag(), false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    };
}
