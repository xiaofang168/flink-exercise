package util

object Helper {

  def autoClose[A <: AutoCloseable, B](closeable: A)(fun: A => B): B = {
    try {
      fun(closeable)
    } finally {
      closeable.close()
    }
  }

}
