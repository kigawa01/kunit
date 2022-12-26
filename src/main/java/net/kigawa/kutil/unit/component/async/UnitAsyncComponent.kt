package net.kigawa.kutil.unit.component.async

import net.kigawa.kutil.unit.extension.async.AsyncExecutor
import net.kigawa.kutil.unit.extension.identify.UnitIdentify
import java.util.concurrent.Callable
import java.util.concurrent.Future
import java.util.concurrent.FutureTask

interface UnitAsyncComponent {
  fun addAsyncExecutor(asyncClass: Class<out AsyncExecutor>)
  fun removeAsyncExecutor(asyncClass: Class<out AsyncExecutor>)
  fun execute(identify: UnitIdentify<out Any>, runnable: Runnable)
  fun <T> submit(identify: UnitIdentify<out Any>, callable: Callable<T>): Future<T> {
    val future = FutureTask(callable)
    execute(identify, future::run)
    return future
  }
}