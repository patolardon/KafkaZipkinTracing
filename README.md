# KafkaZipkinTracing

First download kafka : 
https://kafka.apache.org/downloads

add kafka bin to path

install java 8

from kafka folder :

run zookeeper-server-start.sh config/server.properties

run kafka-server-start.sh config/zookeeper.properties 

run zipkin
https://zipkin.io/pages/quickstart.html

run docker run -d -p 9411:9411 openzipkin/zipkin

zipkin is on localhost:9411

