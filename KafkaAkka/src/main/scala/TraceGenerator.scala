package kafkaakka

case class TraceGenerator(traceId:String, id:String, kind:String, name:String, timestamp:Long, duration:Int,
                          localEndPoint: localEndPoint, remoteEndPoint: remoteEndPoint, tags: tags)
