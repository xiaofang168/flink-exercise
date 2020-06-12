package tablesql

import org.apache.flink.api.scala.{ExecutionEnvironment, _}
import org.apache.flink.table.api.Table
import org.apache.flink.table.api.scala.BatchTableEnvironment

object TableSqlFileDataDemo {

  def main(args: Array[String]): Unit = {

    val env = ExecutionEnvironment.getExecutionEnvironment
    val tableEnv = BatchTableEnvironment.create(env)

    // read a DataStream from an external source
    val csvInput = env.readCsvFile[(String, String, String)](getClass.getResource("/data.txt").getPath)
    val ds: DataSet[LoginEvent] = csvInput.map(e => LoginEvent(e._1, e._2, e._3))

    tableEnv.createTemporaryView("login_event", ds)

    val count: Table = tableEnv.sqlQuery("SELECT count(distinct uid) FROM login_event")

    val result: DataSet[Long] = tableEnv.toDataSet(count)
    // print the stream & execute the program
    result.print()
  }

  case class LoginEvent(uid: String, device: String, phone: String)

}
