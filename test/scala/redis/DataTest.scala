package redis

import io.tmos.arm.ArmMethods._
import org.junit.Test
import org.rocksdb.{Options, RocksDB}

class DataTest {


  @Test
  def rocksdb(): Unit = {
    val dbPath: String = "/Users/fangjie/rocksdb-data/"

    RocksDB.loadLibrary()

    // use scala-arm managing resources, try with resources
    for {
      options <- manage(new Options().setCreateIfMissing(true))
      rocksDB <- manage(RocksDB.open(options, dbPath))} {

      val key = "Hello".getBytes
      rocksDB.put(key, "World".getBytes)
      println(new String(key) + ":" + new String(rocksDB.get(key)))

      rocksDB.put("SecondKey".getBytes, "SecondValue".getBytes)

      val keys = java.util.Arrays.asList(key, "SecondKey".getBytes, "missKey".getBytes)
      val values = rocksDB.multiGetAsList(keys)

      for (i <- 0 to keys.size() - 1) {
        // s插值操作, s实际上是一个方法，除了s还有f，raw（不会对字符串进行转义）
        println(s"multiGet ${new String(keys.get(i))}: ${if (values.get(i) != null) new String(values.get(i)) else null}")
      }

      // 打印全部[key - value]
      var iter = rocksDB.newIterator
      iter.seekToFirst()
      while (iter.isValid) {
        println(s"iterator key: ${new String(iter.key)}, iter value: ${new String(iter.value)}")
        iter.next()
      }
      // 删除一个key
      rocksDB.delete(key)
      println(s"after remove key:${new String(key)}")
      iter = rocksDB.newIterator
      iter.seekToFirst()
      while (iter.isValid) {
        println(s"iterator key: ${new String(iter.key)}, iter value:${new String(iter.value)}")
        iter.next()
      }
    }
  }

}
