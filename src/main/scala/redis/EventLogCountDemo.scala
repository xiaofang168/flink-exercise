package redis

import mate.EventLog
import org.apache.flink.api.common.restartstrategy.RestartStrategies
import org.apache.flink.contrib.streaming.state.RocksDBStateBackend
import org.apache.flink.streaming.api.environment.CheckpointConfig
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
import org.apache.flink.streaming.api.{CheckpointingMode, TimeCharacteristic}

object EventLogCountDemo {

  val dbPath: String = "file:///Users/fangjie/rocksdb-data/"

  def main(args: Array[String]): Unit = {
    // 构造环境
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val config = env.getCheckpointConfig
    // set mode to exactly-once (this is the default)
    config.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE)
    // 退出不删除checkpoint
    config.enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION)

    env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime)
    env.setStateBackend(new RocksDBStateBackend(dbPath, false))
    // start a checkpoint every 2seconds
    env.enableCheckpointing(2000)
    // 重启策略
    env.setRestartStrategy(RestartStrategies.noRestart())

    // 获取数据流
    val redisDataStream: DataStream[Set[EventLog]] = env.addSource(new RedisQueueSource())
      .name("redis-data-source")

    // 数据转换Transformation
    val result: DataStream[(Long, Int)] = redisDataStream.flatMap(e => e)
      .map(e => (e.uid, 1))
      .keyBy(0)
      .sum(1)
      .name("data-count")

    // 数据输出
    result
      .addSink(new SumSink)
      .name("send-result")

    env.execute("read-data-from-redis")
  }

}
