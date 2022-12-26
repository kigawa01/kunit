package net.kigawa.kutil.unit.extension.async

import net.kigawa.kutil.unit.component.UnitIdentify

interface UnitAsyncExecutor {
  fun execute(identify: UnitIdentify<out Any>, runnable: Runnable):Boolean
}