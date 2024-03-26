package net.kigawa.kutil.kunit.impl.concurrent

import java.util.*
import java.util.concurrent.TimeUnit

@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
class ThreadLock {
  @Synchronized
  fun signeAll() {
    (this as Object).notifyAll()
  }
  @Synchronized
  fun block(time: Long, timeUnit: TimeUnit) {
    (this as Object).wait(timeUnit.toMillis(time))
  }
}