package net.kigawa.kutil.unit.extension.logger

import net.kigawa.kutil.unit.annotation.LateInit
import net.kigawa.kutil.unit.util.Util
import java.io.PrintStream
import java.util.logging.Level

@LateInit
class ContainerStdLogger: ContainerLogger {
  override fun log(message: Message) {
    when (message.level) {
      Level.WARNING->log(System.err, message)
      Level.SEVERE ->log(System.err, message)
      else         ->log(System.out, message)
    }
  }
  
  @Synchronized
  private fun log(stream: PrintStream, message: Message) {
    val list = if (message.message == null) Util.createStringList(message.item)
    else Util.createStringList(message.message, message.item)
    list.forEach(stream::println)
    message.cause?.printStackTrace(stream)
  }
}