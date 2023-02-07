package net.kigawa.kutil.unit.extension

import net.kigawa.kutil.unitapi.annotation.getter.LateInit
import net.kigawa.kutil.unitapi.extention.Message
import net.kigawa.kutil.unitapi.extention.UnitLogger
import net.kigawa.kutil.unitapi.util.Util
import java.io.PrintStream
import java.util.logging.Level

@LateInit
class UnitStdLogger: UnitLogger {
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
    else Util.createStringList(message.message!!, message.item)
    list.forEach(stream::println)
    message.cause?.printStackTrace(stream)
  }
}