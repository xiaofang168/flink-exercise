package redis

import java.util
import java.util.stream.Collectors

import com.alibaba.fastjson.JSON
import mate.EventLog

/**
 * 需要加入Scala sdk，不然java语法编译不过，sdk编程编译时，jre运行时
 */
object RunJava {

  def main(args: Array[String]): Unit = {
    val myList = java.util.Arrays.asList("a1", "a2", "b1", "c2", "c1")
    myList.stream.filter(s => s.startsWith("c"))
      .map(_.toUpperCase)
      .sorted
      .forEach(println)

    val b: util.HashSet[String] = new util.HashSet[String]
    b.add("test")
    val c: java.util.Set[EventLog] = b.stream().map[EventLog](e => EventLog(1, "", 1))
      .collect(Collectors.toSet[EventLog])

    println(c)

    b.add("ddd")
    b.add("cc")
    b.stream()
      .filter(e => e.equals("cc"))
      .forEach(println(_))

    val eventLog: EventLog = JSON.parseObject("", EventLog.getClass)
    println(eventLog)
  }

}
