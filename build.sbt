name := "KafkaZipkinTracing"

version := "0.1"

scalaVersion := "2.12.11"

libraryDependencies += "org.apache.kafka" % "kafka-clients" % "2.5.0"
// https://mvnrepository.com/artifact/org.apache.kafka/kafka

libraryDependencies += "io.zipkin.contrib.brave" % "brave-kafka-interceptor" % "0.5.4"

libraryDependencies += "io.zipkin.brave" % "brave-instrumentation-kafka-clients" % "5.9.0"
// https://mvnrepository.com/artifact/io.zipkin.brave/brave-instrumentation-kafka-streams
libraryDependencies += "io.zipkin.brave" % "brave-instrumentation-kafka-streams" % "5.12.4"

// https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-sleuth-zipkin
libraryDependencies += "org.springframework.cloud" % "spring-cloud-sleuth-zipkin" % "2.2.4.RELEASE"
