package redis

import redis.clients.jedis.{Jedis, JedisPool, JedisPoolConfig}

object JedisClient {

  def jedis[T](execute: Jedis => T): T = {
    val jedisPool = new JedisPool(getDefaultJedisPoolConfig, "localhost", 6379, 3000)
    autoClose(jedisPool.getResource()) { jedis =>
      execute(jedis)
    }
  }

  private def getDefaultJedisPoolConfig(): JedisPoolConfig = {
    val jconfig = new JedisPoolConfig
    jconfig.setMaxIdle(20)
    jconfig.setTestOnBorrow(true)
    jconfig.setMaxTotal(100)
    jconfig.setMaxWaitMillis(1000)
    jconfig.setTestWhileIdle(false)
    jconfig
  }

  private def autoClose[A <: AutoCloseable, B](closeable: A)(fun: (A) => B): B = {
    try {
      fun(closeable)
    } finally {
      closeable.close()
    }
  }

}
