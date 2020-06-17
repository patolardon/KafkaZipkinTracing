name := "KafkaZipkinTracing"

version := "0.1"

scalaVersion := "2.12.11"

libraryDependencies += "org.apache.kafka" % "kafka-clients" % "2.5.0"
// https://mvnrepository.com/artifact/io.zipkin.brave/brave-instrumentation-kafka-clients
libraryDependencies += "io.zipkin.brave" % "brave-instrumentation-kafka-clients" % "5.12.3"
libraryDependencies += "io.opentracing.brave" % "brave-opentracing" % "0.37.2"
// https://mvnrepository.com/artifact/io.zipkin.brave/brave-okhttp
libraryDependencies += "io.zipkin.brave" % "brave-okhttp" % "4.13.6"
// https://mvnrepository.com/artifact/io.zipkin.brave/brave
libraryDependencies += "io.zipkin.brave" % "brave" % "5.12.3"
// https://mvnrepository.com/artifact/io.zipkin.reporter2/zipkin-sender-okhttp3
libraryDependencies += "io.zipkin.reporter2" % "zipkin-sender-okhttp3" % "2.15.0"
// https://mvnrepository.com/artifact/io.zipkin.reporter/zipkin-sender-urlconnection
//libraryDependencies += "io.zipkin.reporter" % "zipkin-sender-urlconnection" % "1.1.2"
// https://mvnrepository.com/artifact/io.zipkin.reporter2/zipkin-sender-urlconnection
libraryDependencies += "io.zipkin.reporter2" % "zipkin-sender-urlconnection" % "2.15.0"
