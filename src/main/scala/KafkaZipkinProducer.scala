package kafka.tracing
package kafka.tracing
import zipkin2.reporter.kafka11.KafkaSender
import brave.Tracing
import brave.kafka.clients.KafkaTracing
import brave.sampler.Sampler
import zipkin2.reporter.urlconnection.URLConnectionSender
import java.util.Properties

import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer._
import org.apache.kafka.common.serialization.StringSerializer
import zipkin2.Span
import zipkin2.reporter.brave.AsyncZipkinSpanHandler
import zipkin2.reporter.okhttp3.OkHttpSender
import zipkin2.reporter.{AsyncReporter, Reporter}



object KafkaZipkinProducer {
  def main(args: Array[String]): Unit = {
    /* START TRACING INSTRUMENTATION */
    val sender = OkHttpSender.create("http://localhost:8080/api/v2/spans")
    //val sender = KafkaSender.newBuilder.bootstrapServers("127.0.0.1:9092").topic("zipkin").build
    val zipkinSpanHandler = AsyncZipkinSpanHandler.create(sender) // don't forget to close!
    val tracing = Tracing.newBuilder.localServiceName("my-service").sampler(Sampler.create(1.0F)).addSpanHandler(zipkinSpanHandler).build
    val kafkaTracing = KafkaTracing.newBuilder(tracing).remoteServiceName("kafka").build
    /* END TRACING INSTRUMENTATION */
    val properties = new Properties
    properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092")
    properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    //create producer
    val kafkaProducer = new KafkaProducer[String, String](properties)
    val tracingProducer = kafkaTracing.producer[String, String](kafkaProducer)
    for (i <- 0 to 1000) {
    val record = new ProducerRecord[String, String]("my-topic-test", "1", "hello, world !")

      tracingProducer.send(record)
      tracingProducer.flush()
      zipkinSpanHandler.flush()

    }
    tracingProducer.close()
    zipkinSpanHandler.close()
  }
}