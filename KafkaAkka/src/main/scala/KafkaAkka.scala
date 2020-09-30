package kafkaakka

import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord, KafkaConsumer}
import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}
import java.util
import java.util.Properties
import akka.actor.{ActorSystem, Props}
import scala.collection.JavaConversions._


object KafkaAkka extends App{
  val properties = new Properties

  properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092")
  properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer].getName)
  properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer].getName)
  properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "gfgggfgfgfgffdgd")
  properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

  //create consumer
  val kafkaConsumer = new KafkaConsumer[String, String](properties)

  // create actor

  val system = ActorSystem("Pierre")
  val actor = system.actorOf (Props[ActorConfig], "MySimpleActor")

  kafkaConsumer.subscribe(util.Arrays.asList("my-topic-test"))
  while (true) {
    val consumerRecords = kafkaConsumer.poll(100)
    for (record <- consumerRecords) {
      actor ! record.value()
    }
  }
  kafkaConsumer.close()

}