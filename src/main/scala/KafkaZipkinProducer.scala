import brave.Tracing
import brave.kafka.clients.KafkaTracing
import brave.sampler.Sampler
import zipkin2.reporter.urlconnection.URLConnectionSender
import java.util.Properties

import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer._
import org.apache.kafka.common.serialization.StringSerializer
import zipkin2.Span
import zipkin2.reporter.{AsyncReporter, Reporter}



object KafkaZipkinProducer {
  def main(args: Array[String]): Unit = {
    /* START TRACING INSTRUMENTATION */
    val sender: URLConnectionSender = URLConnectionSender.newBuilder.endpoint("http://127.0.0.1:9411/api/v2/spans").build
    val reporter = AsyncReporter.builder(sender).build
    // this logs in the console
    val tracing = Tracing.newBuilder.localServiceName("hello-producer").sampler(Sampler.ALWAYS_SAMPLE).spanReporter(Reporter.CONSOLE).build
    // this logs nowhere
    //val tracing = Tracing.newBuilder.localServiceName("hello-producer").sampler(Sampler.ALWAYS_SAMPLE).spanReporter(reporter).build
    val kafkaTracing = KafkaTracing.newBuilder(tracing).remoteServiceName("kafka").build
    /* END TRACING INSTRUMENTATION */
    val properties = new Properties
    properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092")
    properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    //create producer
    val kafkaProducer = new KafkaProducer[String, String](properties)
    val tracingProducer = kafkaTracing.producer[String, String](kafkaProducer)
    val record = new ProducerRecord[String, String]("my-topic-test", "1", "hello, world !")

    tracingProducer.send(record)
    tracingProducer.flush()
    tracingProducer.close()
  }
}