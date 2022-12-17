package net.kigawa.kutil.unit.component.info

import net.kigawa.kutil.unit.extension.getter.UnitGetter
import net.kigawa.kutil.unit.extension.identify.UnitIdentify

interface UnitInfo<T> {
  companion object {
    @JvmStatic
    fun <T> create(identify: UnitIdentify<T>, getter: UnitGetter<T>): UnitInfo<T> {
      return UnitInfoImpl(identify)
    }
  }
  
  val identify: UnitIdentify<T>
  val getter: UnitGetter<T>
  var fail: Boolean
  var ready: Boolean
}