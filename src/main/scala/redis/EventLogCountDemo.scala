package redis

import mate.EventLog
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}

object EventLogCountDemo {

  def main(args: Array[String]): Unit = {
    // 构造成对象
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val a: DataStream[Set[EventLog]] = env.addSource(new RedisQueueSource())
  }

}
