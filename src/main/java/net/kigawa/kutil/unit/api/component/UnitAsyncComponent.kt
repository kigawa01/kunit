package net.kigawa.kutil.unit.api.component

import net.kigawa.kutil.unit.api.extention.UnitAsyncExecutor
import net.kigawa.kutil.unit.component.UnitIdentify
import java.util.concurrent.*

interface UnitAsyncComponent: ComponentHolder<UnitAsyncExecutor> {
  fun execute(identify: UnitIdentify<out Any>, runnable: Runnable)
  fun <T> submit(identify: UnitIdentify<out Any>, callable: Callable<T>): Future<T> {
    val future = FutureTask(callable)
    execute(identify, future::run)
    return future
  }
}