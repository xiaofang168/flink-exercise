package redis

import java.util.stream.Collectors

import com.alibaba.fastjson.JSON
import mate.EventLog
import org.apache.flink.streaming.api.functions.source.SourceFunction
import redis.JedisClient.jedis

import scala.collection.JavaConverters._

/**
 * redis 队列源
 */
class RedisQueueSource extends SourceFunction[Set[EventLog]] {

  private var isRunning = true

  private val SLEEP_MILLION = 5000

  private val REDIS_KEY = "log"

  private val LIMIT = 10

  override def run(sourceContext: SourceFunction.SourceContext[Set[EventLog]]): Unit = {
    // 循环读取
    while (isRunning) {
      // 读取队列消息
      val data: java.util.Set[String] = jedis(jedis => {
        jedis.zrevrange(REDIS_KEY, 0, LIMIT)
      })

      // 数据为空休眠一段时间
      if (data == null || data.isEmpty) {
        Thread.sleep(SLEEP_MILLION)
      }

      // 数据反序列化
      val eventLogSet: java.util.Set[EventLog] = data.stream()
        .map[EventLog](JSON.parseObject(_, classOf[EventLog]))
        .collect(Collectors.toSet[EventLog])

      // 收集数据
      sourceContext.collect(eventLogSet.asScala.toSet)
    }
  }

  override def cancel(): Unit = {
    isRunning = false
  }


}
