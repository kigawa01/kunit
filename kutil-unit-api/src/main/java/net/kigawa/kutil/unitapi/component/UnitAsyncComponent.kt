package net.kigawa.kutil.unitapi.component

import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.extention.UnitAsyncExecutor
import java.util.concurrent.*

@Deprecated("not use")
interface UnitAsyncComponent: ComponentHolder<UnitAsyncExecutor> {
  fun execute(identify: UnitIdentify<out Any>, runnable: Runnable)
  @Suppress("unused")
  fun <T> submit(identify: UnitIdentify<out Any>, callable: Callable<T>): Future<T> {
    val future = FutureTask(callable)
    execute(identify, future::run)
    return future
  }
}