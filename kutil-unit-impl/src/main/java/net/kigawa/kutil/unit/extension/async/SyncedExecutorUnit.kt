package net.kigawa.kutil.unit.extension.async

import net.kigawa.kutil.unitapi.annotation.getter.LateInit
import net.kigawa.kutil.unitapi.component.UnitIdentify
import net.kigawa.kutil.unitapi.extention.UnitAsyncExecutor

@LateInit
class SyncedExecutorUnit: UnitAsyncExecutor {
  override fun execute(identify: UnitIdentify<out Any>, runnable: Runnable): Boolean {
    runnable.run()
    return true
  }
}