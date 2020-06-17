import java.lang.System.out
import java.util.Properties

import brave.Tracing
import brave.kafka.clients.KafkaTracing
import brave.sampler.Sampler
import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}
import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}
import zipkin2.reporter.{AsyncReporter, Reporter}
import zipkin2.reporter.urlconnection.URLConnectionSender
import java.util

import org.apache.kafka.clients.consumer.KafkaConsumer
import java.util.Properties

import scala.collection.JavaConverters._
import scala.concurrent.duration.Duration

object KafkaZipkinConsumer extends App{
  /* START TRACING INSTRUMENTATION */
  val sender: URLConnectionSender = URLConnectionSender.newBuilder.endpoint("http://127.0.0.1:9411/api/v2/spans").build
  val reporter = AsyncReporter.builder(sender).build
  val tracing = Tracing.newBuilder.localServiceName("hello-producer").sampler(Sampler.ALWAYS_SAMPLE).spanReporter(Reporter.CONSOLE).build
  val kafkaTracing = KafkaTracing.newBuilder(tracing).remoteServiceName("kafka").build
  /* END TRACING INSTRUMENTATION */
  val properties = new Properties

  properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092")
  properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer].getName)
  properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer].getName)
  properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "mygroup1")
  properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
  //create producer
  val kafkaConsumer = new KafkaConsumer[String, String](properties)
  val tracingConsumer = kafkaTracing.consumer(kafkaConsumer)
  tracingConsumer.subscribe(util.Arrays.asList("my-topic-test"))
  while (true) {
    val consumerRecords = tracingConsumer.poll(100)
    import scala.collection.JavaConversions._
    for (record <- consumerRecords) {
      val span = kafkaTracing.nextSpan(record).name("print-hello").start()
      span.annotate("starting printing")
      println(String.format("Record: %s", record))
      span.annotate("printing finished")
      span.finish()
    }
  }
}