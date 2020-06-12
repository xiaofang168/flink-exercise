package wordcount

import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.time.Time

/**
 * socket 流处理
 * <p>
 * first run with mac shell: nc -lk 9000
 * import org.apache.flink.api.scala.{ExecutionEnvironment, _}
 * </p>
 */
object SocketWindowWordCount {

  def main(args: Array[String]): Unit = {
    // 创建execution environment
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    // 通过连接socket 获取输入数据，这里连接到本地9000 端口，如果9000端口占用，换一个端口
    val text = env.socketTextStream("localhost", 9000)
    val windowCounts = text
      .flatMap(w => w.split("\\s"))
      .map(w => (w, 1))
      .keyBy(0)
      .timeWindow(Time.seconds(60))
      .sum(1)
    windowCounts.print().setParallelism(1)
    env.execute("Socket Window WordCount")
  }

}
