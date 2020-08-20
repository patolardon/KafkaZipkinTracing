package main.scala

import java.lang.System.out
import java.util.Properties

import brave.Tracing
import brave.kafka.clients.KafkaTracing
import brave.sampler.Sampler
import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}
import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}
import zipkin2.reporter.{AsyncReporter, Reporter}
import java.util

import org.apache.kafka.clients.consumer.KafkaConsumer
import java.util.Properties

import zipkin2.reporter.kafka.KafkaSender


object KafkaZipkinConsumer extends App{
  /* START TRACING INSTRUMENTATION */
  //val sender = OkHttpSender.create("http://localhost:8080/api/v2/spans")
  val sender = KafkaSender.newBuilder.bootstrapServers("127.0.0.1:9092").topic("zipkin").build
  val zipkinSpanHandler = AsyncReporter.create(sender)  // don't forget to close!
  val tracing = Tracing.newBuilder.localServiceName("my-service").sampler(Sampler.create(1.0F)).spanReporter(zipkinSpanHandler).build
  val kafkaTracing = KafkaTracing.newBuilder(tracing).remoteServiceName("kafka").build

  /* END TRACING INSTRUMENTATION */
  val properties = new Properties

  properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092")
  properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer].getName)
  properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer].getName)
  properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "gfgggfgfgfgffdgd")
  properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
  properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

  //create consumer
  val kafkaConsumer = new KafkaConsumer[String, String](properties)
  val tracingConsumer = kafkaTracing.consumer(kafkaConsumer)

  tracingConsumer.subscribe(util.Arrays.asList("my-topic-test"))
  while (true) {
    val consumerRecords = tracingConsumer.poll(100)
    import scala.collection.JavaConversions._
    for (record <- consumerRecords) {
      println(String.format("Record: %s", record))
    }
  }
  zipkinSpanHandler.close()

}