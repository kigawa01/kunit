package net.kigawa.kutil.unit.extension.async

import net.kigawa.kutil.unit.extension.identify.UnitIdentify

class SyncedExecutor: AsyncExecutor {
  override fun execute(identify: UnitIdentify<out Any>, runnable: Runnable): Boolean {
    runnable.run()
    return true
  }
}