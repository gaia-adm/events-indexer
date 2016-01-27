[![Circle CI](https://circleci.com/gh/gaia-adm/events-indexer.svg?style=svg)](https://circleci.com/gh/gaia-adm/events-indexer) [![Codacy Badge](https://api.codacy.com/project/badge/grade/28faac6a717e48079a1cc0aae3959eb5)](https://www.codacy.com/app/alexei-led/events-indexer) [![](https://badge.imagelayers.io/gaiaadm/event-indexer:latest.svg)](https://imagelayers.io/?images=gaiaadm/event-indexer:latest 'Get your own badge on imagelayers.io')

Preface
=======
This service reads data from Rabbitmq queue named __events-indexer__ and writes the data into influxdb.
It uses Rabbitmq header named __dbname__ to resolve the db to write into in influxdb

It supports InfluxDB 0.9 write API only (it won't work on InfluxDB 0.8)
Check out the testing sections in circle.yml in to root directory, to see how you can use this service.
