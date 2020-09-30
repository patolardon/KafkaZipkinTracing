package kafkaakka

import java.net.URLEncoder

import akka.actor.Actor
import net.liftweb.json._
import net.liftweb.json.Serialization.write

import scala.util.Random
import scala.concurrent.ExecutionContext.Implicits.global
import akka.http.scaladsl.client.RequestBuilding.Post
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.model.headers.RawHeader

class ActorConfig extends Actor {
  def receive = {
    case s: String => {
      val id = Random.alphanumeric.take(8).mkString("").toList.map(_.toInt.toHexString).mkString
      val timestamp = System.currentTimeMillis / 1000
      val requete = postZipkin.post(postZipkin.getData(id, timestamp))
      println(requete)
      println(postZipkin.getData(id, timestamp))
    }
  }
}

object postZipkin {


  def post(data:String) = Post("http://localhost:9411/api/v2/spans", data)


  def getData(id:String, timestamp:Long):String = {
    val data = TraceGenerator(traceId = id,
      id = id, kind = "PRODUCER", name = "Name", timestamp = timestamp,
      duration = 2,
      localEndPoint = localEndPoint("kafka"), remoteEndPoint = remoteEndPoint("kafka"), tags = tags("1", "my-topic-test")
    )
    implicit val formats = DefaultFormats
    val textData = write(data)
    s"[$textData]"
  }
}


