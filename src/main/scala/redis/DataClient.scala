package redis

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.serializer.SerializeConfig
import mate.EventLog
import redis.JedisClient._

/**
 * 需要2.13版本才支持java 8 stream语法，2.12不支持
 */
object DataClient {

  def main(args: Array[String]): Unit = {
    jedis(jedis => {
      val eventLog = EventLog(121, "13740776181", 0)
      val conf = new SerializeConfig(true)
      jedis.zadd("log", System.currentTimeMillis(), JSON.toJSONString(eventLog, conf))
    })
  }

}
