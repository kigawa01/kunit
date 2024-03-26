package net.kigawa.kutil.kunit.impl.extension

import net.kigawa.kutil.kunit.api.annotation.getter.LateInit
import net.kigawa.kutil.kunit.api.extention.Message
import net.kigawa.kutil.kunit.api.extention.UnitLogger
import java.io.PrintStream
import java.util.logging.Level

@LateInit
class UnitStdLogger : UnitLogger {
  override fun log(message: Message) {
    when (message.level) {
      Level.WARNING -> log(System.err, message)
      Level.SEVERE -> log(System.err, message)
      else -> log(System.out, message)
    }
  }

  @Synchronized
  private fun log(stream: PrintStream, message: Message) {
    stream.print("${message.level}: ")
    message.message?.let(stream::println)
    message.items.filterNotNull().forEach(stream::println)
    message.cause.filterNotNull().forEach(stream::println)
  }
}