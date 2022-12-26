package net.kigawa.kutil.unit.extension.logger

import net.kigawa.kutil.unit.concurrent.ConcurrentList

class ContainerMessageStore: ContainerLogger {
  private val messages = ConcurrentList<Message>()
  override fun log(message: Message) {
    messages.add(message)
  }
}