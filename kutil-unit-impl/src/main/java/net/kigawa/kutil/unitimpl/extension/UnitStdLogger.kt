package net.kigawa.kutil.unitimpl.extension

import net.kigawa.kutil.unitapi.annotation.getter.LateInit
import net.kigawa.kutil.unitapi.extention.Message
import net.kigawa.kutil.unitapi.extention.UnitLogger
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
    message.message?.let(stream::println)
    message.items.filterNotNull().forEach(stream::println)
    message.cause.filterNotNull().forEach(stream::println)
  }
}