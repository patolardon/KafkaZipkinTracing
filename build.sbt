name := "KafkaZipkinTracing"

version := "0.1"

scalaVersion := "2.12.11"

libraryDependencies += "org.apache.kafka" % "kafka-clients" % "2.5.0"

libraryDependencies += "io.zipkin.contrib.brave" % "brave-kafka-interceptor" % "0.5.2"
