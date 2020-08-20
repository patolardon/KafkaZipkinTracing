name := "KafkaZipkinTracing"

version := "0.1"

scalaVersion := "2.12.11"

lazy val KafkaStreamsZipkin = project
  .settings(
    name := "KafkaStreamsZipkin",
    libraryDependencies ++= Seq(
      "org.apache.kafka" %% "kafka-streams-scala" % "2.4.1",
      "io.zipkin.brave" % "brave-instrumentation-kafka-streams" % "5.12.4",
      "io.zipkin.contrib.brave" % "brave-kafka-interceptor" % "0.5.4"
    )
  )


lazy val KafkaZipkin = project
  .settings(
    name := "KafkaZipkin",
    libraryDependencies ++= Seq(
      "org.apache.kafka" % "kafka-clients" % "2.5.0",
      "io.zipkin.contrib.brave" % "brave-kafka-interceptor" % "0.5.4",
      "io.zipkin.brave" % "brave-instrumentation-kafka-clients" % "5.9.0",
      "io.zipkin.brave" % "brave-instrumentation-kafka-streams" % "5.12.4",
      "org.springframework.cloud" % "spring-cloud-sleuth-zipkin" % "2.2.4.RELEASE"
    )
  )