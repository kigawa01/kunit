package net.kigawa.kutil.unit.concurrent

import java.util.*
import java.util.concurrent.TimeUnit

@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
class ThreadBlocker {
  @Synchronized
  fun signeAll() {
    (this as Object).notifyAll()
  }
  
  @Suppress("unused")
  @Synchronized
  fun signe() {
    (this as Object).notify()
  }
  
  @Synchronized
  fun block(time: Long, timeUnit: TimeUnit) {
    (this as Object).wait(timeUnit.toMillis(time))
  }
}