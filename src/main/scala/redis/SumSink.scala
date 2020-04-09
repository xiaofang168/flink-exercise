package redis

import org.apache.flink.annotation.PublicEvolving
import org.apache.flink.streaming.api.functions.sink.SinkFunction

@PublicEvolving
class SumSink extends SinkFunction[(Long, Int)] {

  override def invoke(value: (Long, Int), context: SinkFunction.Context[_]): Unit = {
    println("count>>>:" + value.toString())
  }

}
