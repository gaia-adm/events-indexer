CircleCI build status: [![Circle CI](https://circleci.com/gh/gaia-adm/events-indexer.svg?style=svg)](https://circleci.com/gh/gaia-adm/events-indexer)

Preface
=======
This service reads data from Rabbitmq queue named __events-indexer__ and writes the data into influxdb.
It uses Rabbitmq header named __dbname__ to resolve the db to write into in influxdb

Check out the testing sections in circle.yml in to root directory (although it is grayed out) to see how you can use this service
