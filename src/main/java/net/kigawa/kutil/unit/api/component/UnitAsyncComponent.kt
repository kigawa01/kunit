package net.kigawa.kutil.unit.api.component

import net.kigawa.kutil.unit.component.UnitIdentify
import net.kigawa.kutil.unit.extension.async.UnitAsyncExecutor
import java.util.concurrent.Callable
import java.util.concurrent.Future
import java.util.concurrent.FutureTask

interface UnitAsyncComponent {
  fun addAsyncExecutor(asyncClass: Class<out UnitAsyncExecutor>)
  fun removeAsyncExecutor(asyncClass: Class<out UnitAsyncExecutor>)
  fun execute(identify: UnitIdentify<out Any>, runnable: Runnable)
  fun <T> submit(identify: UnitIdentify<out Any>, callable: Callable<T>): Future<T> {
    val future = FutureTask(callable)
    execute(identify, future::run)
    return future
  }
}