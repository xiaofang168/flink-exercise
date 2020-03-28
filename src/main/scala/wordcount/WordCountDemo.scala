package wordcount

import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.api.scala.{ExecutionEnvironment, _}

object WordCountDemo {

  def main(args: Array[String]): Unit = {
    // Checking input parameters
    val params = ParameterTool.fromArgs(args)

    // set up the execution environment
    val env = ExecutionEnvironment.getExecutionEnvironment

    // make parameters available in the web interface
    env.getConfig.setGlobalJobParameters(params)

    // get input data
    val text =
      if (params.has("input")) {
        env.readTextFile(params.get("input"))
      } else {
        println("Executing WordCount example with default input data set.")
        println("Use --input to specify file input.")
        env.fromCollection(WordCountData.WORDS)
      }

    val counts = text.flatMap(_.toLowerCase.split("\\W+")
      .filter(_.nonEmpty))
      .map((_, 1))
      .groupBy(0)
      .sum(1)

    if (params.has("output")) {
      counts.writeAsCsv(params.get("output"), "\n", " ")
      env.execute("Scala WordCount Example")
    } else {
      println("Printing result to stdout. Use --output to specify output path.")
      counts.print()
    }

  }

}
