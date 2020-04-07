package redis

import org.junit.Test
import org.rocksdb.{Options, RocksDB}
import util.Helper._

class DataTest {

  @Test
  def rocksdb(): Unit = {
    val dbPath: String = "/Users/fangjie/rocksdb-data/"

    RocksDB.loadLibrary()

    autoClose(new Options().setCreateIfMissing(true)) { options =>

      autoClose(RocksDB.open(options, dbPath)) { rocksDB =>
        val key = "Hello".getBytes
        rocksDB.put(key, "World".getBytes)
        println(new String(key) + ":" + new String(rocksDB.get(key)))

        rocksDB.put("SecondKey".getBytes, "SecondValue".getBytes)

        val keys = java.util.Arrays.asList(key, "SecondKey".getBytes, "missKey".getBytes)
        val values = rocksDB.multiGetAsList(keys)
        var i = 0

        while (i < keys.size) {
          println("multiGet " + new String(keys.get(i)) + ":"
            + (if (values.get(i) != null) new String(values.get(i)) else null))
          i += 1
        }
        // 打印全部[key - value]
        var iter = rocksDB.newIterator
        iter.seekToFirst()
        while (iter.isValid) {
          println("iterator key:" + new String(iter.key) + ", iter value:" + new String(iter.value))
          iter.next()
        }
        // 删除一个key
        rocksDB.delete(key)
        println("after remove key:" + new String(key))
        iter = rocksDB.newIterator
        iter.seekToFirst()
        while (iter.isValid) {
          System.out.println("iterator key:" + new String(iter.key) + ", iter value:" + new String(iter.value))
          iter.next()
        }
      }

    }
  }

}