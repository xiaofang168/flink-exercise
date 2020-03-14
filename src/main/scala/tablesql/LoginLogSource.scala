package tablesql

import org.apache.flink.streaming.api.functions.source.SourceFunction

class LoginLogSource extends SourceFunction[String] {
  override def run(sourceContext: SourceFunction.SourceContext[String]): Unit = ???

  override def cancel(): Unit = ???
}
