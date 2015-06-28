FROM java:8-jre

ENV GAIA_HOME=/usr/local/gaia
WORKDIR $GAIA_HOME

COPY ./target/event-indexer.jar $GAIA_HOME

CMD java -jar $GAIA_HOME/targer/event-indexer.jar server