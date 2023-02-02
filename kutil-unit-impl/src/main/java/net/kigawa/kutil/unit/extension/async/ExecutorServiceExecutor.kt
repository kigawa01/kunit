package net.kigawa.kutil.unit.extension.async

import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.extention.UnitAsyncExecutor
import java.util.concurrent.ExecutorService

@Suppress("unused")
class ExecutorServiceExecutor(private val executorService: ExecutorService): UnitAsyncExecutor {
  override fun execute(identify: UnitIdentify<out Any>, runnable: Runnable): Boolean {
    executorService.execute(runnable)
    return true
  }
}