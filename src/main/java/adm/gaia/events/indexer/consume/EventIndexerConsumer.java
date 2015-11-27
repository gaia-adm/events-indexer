package adm.gaia.events.indexer.consume;

import adm.gaia.events.indexer.managers.InfluxDBManager;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
                Thread.currentThread().toString() + ", Received: " + body.length + " bytes");

        try {
            String dbName = props.getHeaders().get("dbname").toString();
            if (dbName == null) {
                System.err.println("Rabbitmq dbname header property was empty, sending Nack to RabbitMQ");
                getChannel().basicNack(envelope.getDeliveryTag(), false, false);
            }

            WebTarget webTarget = influxDBManager.getJerseyClient().target(influxDBManager.getInfluxDbBaseUrl() + dbName + influxDBManager.getInfluxDbQueryParams());
            Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).
                                             post(Entity.entity(body, MediaType.TEXT_PLAIN_TYPE));

            if (response.getStatus() == 204) {
                getChannel().basicAck(envelope.getDeliveryTag(), false);
            } else
            {
                System.err.println("Failed to post to InfluxDB: " + response.toString() + ", Sending Nack to RabbitMQ");
                getChannel().basicNack(envelope.getDeliveryTag(), false, false);
            }

       } catch (IOException e) {
            e.printStackTrace();
        }
    };
}
