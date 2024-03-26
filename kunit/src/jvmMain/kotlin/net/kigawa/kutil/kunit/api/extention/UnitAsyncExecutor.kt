package net.kigawa.kutil.kunit.api.extention

import net.kigawa.kutil.kunit.api.UnitIdentify

interface UnitAsyncExecutor {
  fun execute(identify: UnitIdentify<out Any>, runnable: Runnable): Boolean
}