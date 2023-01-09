package net.kigawa.kutil.unit.extension

import net.kigawa.kutil.unit.annotation.getter.LateInit
import net.kigawa.kutil.unit.api.component.UnitInfo
import net.kigawa.kutil.unit.api.extention.UnitCloser

@LateInit
class AutoCloseAbleCloser: UnitCloser {
  override fun closeUnit(info: UnitInfo<out Any>): Boolean {
    if (!info.instanceOf(AutoCloseable::class.java)) return false
    (info.get() as AutoCloseable).close()
    return true
  }
}