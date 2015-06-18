package adm.gaia.events.indexer;

import com.rabbitmq.client.Channel;

import javax.ws.rs.client.WebTarget;

/**
 * Created by tsadok on 18/06/2015.
 */
public class ConsumersRegistrar {

    RabbitmqManager rabbitmqManager;
    InfluxDBManager influxDBManager;

    public ConsumersRegistrar(RabbitmqManager rabbitmqManager, InfluxDBManager influxDBManager) {
        this.influxDBManager = influxDBManager;
        this.rabbitmqManager = rabbitmqManager;
    }

    /*
            Create consumers according to the number of processors available for the JVM
            DefaultConsumer in RabbitMQ is running on a thread coming for a ThreadPool
            RabbitMQ is managing the ThreadPool
            Also we allocate channel per consumer (This is the best practice according to RabbitMQ docs to avoid multi threading problems)

        */
    public void register() throws Exception
    {
            for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
                Channel consumerChannel = rabbitmqManager.getConnection().createChannel();
                consumerChannel.basicConsume(rabbitmqManager.getRabbitmqConf().getQueueName(), false,
                        new EventIndexerConsumer(consumerChannel, influxDBManager));
            }
    }
}
