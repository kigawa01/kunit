package net.kigawa.kutil.unit.extension.async

import net.kigawa.kutil.unit.extension.identify.UnitIdentify

interface AsyncExecutor {
  fun execute(identify: UnitIdentify<out Any>, runnable: Runnable):Boolean
}