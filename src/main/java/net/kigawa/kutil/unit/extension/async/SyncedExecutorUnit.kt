package net.kigawa.kutil.unit.extension.async

import net.kigawa.kutil.unit.component.UnitIdentify

class SyncedExecutorUnit: UnitAsyncExecutor {
  override fun execute(identify: UnitIdentify<out Any>, runnable: Runnable): Boolean {
    runnable.run()
    return true
  }
}