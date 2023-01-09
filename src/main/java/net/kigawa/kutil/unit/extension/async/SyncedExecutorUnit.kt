package net.kigawa.kutil.unit.extension.async

import net.kigawa.kutil.unit.annotation.getter.LateInit
import net.kigawa.kutil.unit.api.extention.UnitAsyncExecutor
import net.kigawa.kutil.unit.component.UnitIdentify

@LateInit
class SyncedExecutorUnit: UnitAsyncExecutor {
  override fun execute(identify: UnitIdentify<out Any>, runnable: Runnable): Boolean {
    runnable.run()
    return true
  }
}