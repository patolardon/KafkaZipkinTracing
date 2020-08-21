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

