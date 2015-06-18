package adm.gaia.events.indexer;

import com.rabbitmq.client.*;
import io.dropwizard.lifecycle.Managed;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * Taking care for the setup and tire down of RabbitMQ connections
 *
 * Created by tsadok on 17/06/2015.
 */
public class RabbitmqManager implements Managed {

    private Connection connection;
    RabbitmqConfiguration rabbitmqConf;

    public RabbitmqManager(EventsIndexerConfiguration configuration) {
        this.rabbitmqConf = configuration.getRabbitmqConfiguration();
    }

    public Connection getConnection() {
        return connection;
    }

    public RabbitmqConfiguration getRabbitmqConf() {
        return rabbitmqConf;
    }

    @Override
    public void start() throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbitmqConf.getHost());
        factory.setPort(rabbitmqConf.getPort());
        factory.setUsername(rabbitmqConf.getUsername());
        factory.setPassword(rabbitmqConf.getPassword());
        factory.setAutomaticRecoveryEnabled(true); // connection that will recover automatically
        factory.setNetworkRecoveryInterval(10000); // attempt recovery every 10 seconds
        connection = factory.newConnection();

        Channel defineChannel = connection.createChannel();
        defineChannel.queueDeclare(rabbitmqConf.getQueueName(), true, false, false, null);


        //Temp code to simulate sending of events
        Channel producerChannel = connection.createChannel();
        for (int i=0; i< 10; i++) {
            String message = "Hello World! - " + i;
            AMQP.BasicProperties props = new AMQP.BasicProperties();
            props.getHeaders().put("dbname", "db2");
            producerChannel.basicPublish("", rabbitmqConf.getRoutingKey(), props, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }

    }

    @Override
    public void stop() throws Exception {

        // gracefully shutting down RabbitMQ connections
        // (Dropwizard will call the stop method when it shutting down)
        if (connection != null)
            connection.close();
    }
}
