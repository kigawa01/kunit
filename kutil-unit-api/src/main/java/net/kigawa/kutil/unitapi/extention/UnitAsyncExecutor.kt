package net.kigawa.kutil.unitapi.extention

import net.kigawa.kutil.unitapi.component.UnitIdentify

interface UnitAsyncExecutor {
  fun execute(identify: UnitIdentify<out Any>, runnable: Runnable): Boolean
}