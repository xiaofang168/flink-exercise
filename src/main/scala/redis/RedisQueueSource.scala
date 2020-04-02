package redis

import org.apache.flink.streaming.api.functions.source.SourceFunction
import redis.JedisClient._

import scala.collection.immutable.HashMap

/**
 * redis 队列源
 */
class RedisQueueSource extends SourceFunction[HashMap[String, String]] {

  override def run(sourceContext: SourceFunction.SourceContext[HashMap[String, String]]): Unit = {
    // 读取队列消息
    val a = jedis(jedis => {
      jedis.zrangeWithScores("log", 0, 10)
    })

  }

  override def cancel(): Unit = ???


}
