package redis

import java.util

import mate.EventLog
import org.apache.flink.streaming.api.scala
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}

object EventLogCountDemo {

  def main(args: Array[String]): Unit = {
    // 构造成对象
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val a: scala.DataStream[util.Set[EventLog]] = env.addSource(new RedisQueueSource())
  }

}
