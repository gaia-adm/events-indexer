package adm.gaia.events.indexer;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by tsadok on 14/06/2015.
 */
public class EventsIndexerConfiguration extends Configuration{

    @JsonProperty("rabbitmq")
    private RabbitmqConfiguration rabbitmqConfiguration = new RabbitmqConfiguration();

    public RabbitmqConfiguration getRabbitmqConfiguration() {
        return rabbitmqConfiguration;
    }

    public void setRabbitmqConfiguration(RabbitmqConfiguration rabbitmqConfiguration) {
        this.rabbitmqConfiguration = rabbitmqConfiguration;
    }
}