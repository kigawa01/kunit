package net.kigawa.kutil.unitimpl.extension

import net.kigawa.kutil.unitapi.annotation.getter.LateInit
import net.kigawa.kutil.unitapi.component.UnitInfo
import net.kigawa.kutil.unitapi.extention.UnitCloser

@LateInit
class AutoCloseAbleCloser: UnitCloser {
  override fun closeUnit(info: UnitInfo<out Any>): Boolean {
    if (!info.instanceOf(AutoCloseable::class.java)) return false
    (info.get() as AutoCloseable).close()
    return true
  }
}