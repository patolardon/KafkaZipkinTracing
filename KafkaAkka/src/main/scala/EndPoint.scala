package kafkaakka

sealed trait EndPoint {
  val serviceName:String
}

case class localEndPoint(override val serviceName: String) extends EndPoint

case class remoteEndPoint(override val serviceName: String) extends EndPoint

