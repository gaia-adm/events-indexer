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

        registerConsumers();

        //Temp code to simulate sending of events
        Channel producerChannel = connection.createChannel();
        for (int i=0; i< 10; i++) {
            String message = "Hello World! - " + i;
            producerChannel.basicPublish("", rabbitmqConf.getRoutingKey(), null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }

    }

    /*
            Create consumers according to the number of processors available for the JVM
            DefaultConsumer in RabbitMQ is running on a thread comming for a ThreadPool
            RabbitMQ is managing the ThreadPool
            Also we allocate channel per consumer (This is the best practice according to RabbitMQ docs to avoid multi threading problems)

        */
    private void registerConsumers() throws Exception
    {
        for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {

            Channel consumerChannel = connection.createChannel();
            consumerChannel.basicConsume(rabbitmqConf.getQueueName(), false, new EventIndexerConsumer(consumerChannel));
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
