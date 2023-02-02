package net.kigawa.kutil.unitapi.extention

import net.kigawa.kutil.unitapi.UnitIdentify

interface UnitAsyncExecutor {
  fun execute(identify: UnitIdentify<out Any>, runnable: Runnable): Boolean
}