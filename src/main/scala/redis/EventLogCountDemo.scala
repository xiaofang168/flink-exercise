package redis

import mate.EventLog
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}

object EventLogCountDemo {

  def main(args: Array[String]): Unit = {
    // 构造环境
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    // 获取数据流
    val redisDataStream: DataStream[Set[EventLog]] = env.addSource(new RedisQueueSource())

    // 数据转换Transformation
    val result = redisDataStream.flatMap(e => e)
      .map(e => (e.uid, 1))
      .keyBy(0)
      .sum(1)

    result.print()

    env.execute("read data from redis")

  }

}
