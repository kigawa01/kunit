package net.kigawa.kutil.unit.extension.logger

import java.io.PrintStream
import java.util.logging.Level

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
    stream.println(message.message)
    message.cause?.printStackTrace(stream)
    message.item.forEach {
      it.let {stream.printf("\t %-20s :$it", it?.javaClass?.simpleName)}
    }
  }
}