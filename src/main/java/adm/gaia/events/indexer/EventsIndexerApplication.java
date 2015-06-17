package adm.gaia.events.indexer;

import com.rabbitmq.client.*;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * Created by tsadok on 14/06/2015.
 */
public class EventsIndexerApplication extends Application<EventsIndexerConfiguration>{


    public static void main(String[] args) throws Exception {
        new EventsIndexerApplication().run(args);
    }

    @Override
    public String getName() {
        return "adm-gaia-events-indexer";
    }

    @Override
    public void initialize(Bootstrap<EventsIndexerConfiguration> bootstrap) {

    }

    @Override
    public void run(EventsIndexerConfiguration configuration,
                    Environment environment)  throws Exception
    {

        RabbitmqManager rabbitmqManager = new RabbitmqManager(configuration);
        environment.lifecycle().manage(rabbitmqManager);

        //final Client client = new JerseyClientBuilder(environment).using(configuration.getJerseyClientConfiguration())
        //        .build(getName());

        /*final Client client = new JerseyClientBuilder(environment).using(new JerseyClientConfiguration())
                .build(getName());

        WebTarget webTarget = client.target("http://192.168.59.103:8086/db/db1/series?u=root&p=root");*/
    }
}






