package main.scala

import brave.Tracing
import brave.sampler.Sampler
import brave.kafka.clients.KafkaTracing
import java.util.Properties

import brave.handler.FinishedSpanHandler
import brave.internal.handler.ZipkinFinishedSpanHandler
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer._
import org.apache.kafka.common.serialization.StringSerializer
import zipkin2.reporter.kafka.KafkaSender
import zipkin2.reporter.okhttp3.OkHttpSender
import zipkin2.reporter.{AsyncReporter, Reporter}



object KafkaZipkinProducer {
  def main(args: Array[String]): Unit = {
    /* START TRACING INSTRUMENTATION */
    val sender = OkHttpSender.create("http://localhost:9411/api/v2/spans")
    //val sender: KafkaSender = KafkaSender.newBuilder.bootstrapServers("127.0.0.1:9092").topic("zipkin").build
    val zipkinSpanHandler = AsyncReporter.create(sender) // don't forget to close!
    val tracing = Tracing.newBuilder.localServiceName("kafka-producer-test").addFinishedSpanHandler(FinishedSpanHandler.NOOP)
      .sampler(Sampler.create(1.0F)).spanReporter(zipkinSpanHandler).build

    val kafkaTracing = KafkaTracing.newBuilder(tracing).remoteServiceName("kafka").build
    /* END TRACING INSTRUMENTATION */
    val properties = new Properties
    properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092")
    properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    //create producer
    val kafkaProducer = new KafkaProducer[String, String](properties)
    val tracingProducer = kafkaTracing.producer[String, String](kafkaProducer)
    for (i <- 0 to 10) {
    val record = new ProducerRecord[String, String]("my-topic-test", "1", "hello, world !")

      tracingProducer.send(record)

    }
    tracingProducer.close()
    zipkinSpanHandler.close()
  }
}