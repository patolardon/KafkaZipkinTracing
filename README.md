# KafkaZipkinTracing

First download kafka : 
https://kafka.apache.org/downloads

add kafka bin to path

install java 8

from kafka folder :

run zookeeper-server-start.sh config/server.properties

run kafka-server-start.sh config/zookeeper.properties 

run KafkaStreamsZipkin app

run KafkaZipkinProducer app

consume topics : my-topic-test, output-topic, zipkin, zipkin-streams (which are automatically created as soon as the producer is writing in my-topic-test) :
  You should see messages and traces

Kafka connect : 

From the KafkaConnectZipkin folder, run the run.sh command.

Only tune was to add brave-kafka-interceptor-0.5.5-SNAPSHOT.jar to the kafka-connect-twitter folder and to parameter it in the connect-standalone.properties
