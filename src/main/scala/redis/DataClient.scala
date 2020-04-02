package redis

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.serializer.SerializeConfig
import mate.EventLog
import redis.JedisClient._

object DataClient {

  def main(args: Array[String]): Unit = {
    jedis(jedis => {
      val eventLog = EventLog(111, "13545776181", 1)
      val conf = new SerializeConfig(true)
      jedis.zadd("log", System.currentTimeMillis(), JSON.toJSONString(eventLog, conf))
    })
  }

}
