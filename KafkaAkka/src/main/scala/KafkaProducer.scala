import java.util.Properties


import org.apache.kafka.clients.producer.{ProducerRecord, _}
import org.apache.kafka.common.serialization.StringSerializer



object KafkaProducer {
  def main(args: Array[String]): Unit = {
    val properties = new Properties
    properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092")
    properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    //create producer
    val kafkaProducer = new KafkaProducer[String, String](properties)
    for (i <- 0 to 10) {
    val record = new ProducerRecord[String, String]("my-topic-test", "1", "hello, world !")

      kafkaProducer.send(record)

    }
    kafkaProducer.close()
  }
}