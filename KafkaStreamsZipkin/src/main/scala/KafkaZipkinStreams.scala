import java.util.Properties

import brave.Tracing
import brave.kafka.streams.KafkaStreamsTracing
import brave.sampler.Sampler
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.common.serialization.Serdes.StringSerde
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.kstream.Produced
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala.Serdes._
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.KStream
import zipkin2.reporter.AsyncReporter
import zipkin2.reporter.kafka.KafkaSender

object KafkaZipkinStreams extends App{
  // create config

  val config: Properties = {
    val properties = new Properties()
    properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "MowerStream")
    val bootstrapServers = if (args.length > 0) args(0) else "localhost:9092"
    properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
    properties.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, classOf[StringSerde].getName)
    properties.setProperty(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, classOf[StringSerde].getName)
    properties
  }

  // create topology
  val streamsBuilder = new StreamsBuilder

  /* START TRACING INSTRUMENTATION */
  val sender: KafkaSender = KafkaSender.newBuilder.bootstrapServers("127.0.0.1:9092").topic("zipkinStreams").build
  val zipkinSpanHandler = AsyncReporter.create(sender) // don't forget to close!
  val tracing = Tracing.newBuilder.localServiceName("my-service")
    .sampler(Sampler.create(1.0F)).spanReporter(zipkinSpanHandler).build
  val kafkaStreamsTracing = KafkaStreamsTracing.create(tracing)
  /* END TRACING INSTRUMENTATION */
  val inputTopic: KStream[String, String] = streamsBuilder.stream[String, String]("my-topic-test")

  import org.apache.kafka.streams.KafkaStreams


  val outputTopic = inputTopic.mapValues( v=> run(v))

  outputTopic.to("output-topic")(Produced.`with`(Serdes.String(), Serdes.String()))

  // build the topology
  val kafkaStreams: KafkaStreams = new KafkaStreams(streamsBuilder.build(), config)
  val kafkaStreamstrace = kafkaStreamsTracing.kafkaStreams(streamsBuilder.build(), config)

  //start the stream
  kafkaStreamstrace.start()

  def run(value:String):String ={
    value + " Pierre !"
  }
}