package net.kigawa.kutil.unit.api.extention

import net.kigawa.kutil.unit.component.UnitIdentify

interface UnitAsyncExecutor {
  fun execute(identify: UnitIdentify<out Any>, runnable: Runnable):Boolean
}