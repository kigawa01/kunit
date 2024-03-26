package net.kigawa.kutil.kunit.api.component

import net.kigawa.kutil.kunit.api.UnitIdentify
import net.kigawa.kutil.kunit.api.extention.UnitAsyncExecutor
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