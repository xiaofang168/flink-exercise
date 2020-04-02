package redis

import java.util.stream.Collectors

import com.alibaba.fastjson.JSON
import mate.EventLog
import org.apache.flink.streaming.api.functions.source.SourceFunction
import redis.JedisClient.jedis


/**
 * redis 队列源
 */
class RedisQueueSource extends SourceFunction[java.util.Set[EventLog]] {

  override def run(sourceContext: SourceFunction.SourceContext[java.util.Set[EventLog]]): Unit = {
    // 读取队列消息
    val data: java.util.Set[String] = jedis(jedis => {
      jedis.zrange("log", 0, 10)
    })

    val eventLogSet: java.util.Set[EventLog] = data.stream()
      .map[EventLog](e => JSON.parseObject(e, EventLog.getClass))
      .collect(Collectors.toSet[EventLog])

    sourceContext.collect(eventLogSet)
  }

  override def cancel(): Unit = ???


}
