package net.kigawa.kutil.unit.component.info

import net.kigawa.kutil.unit.extension.getter.UnitGetter
import net.kigawa.kutil.unit.extension.identify.UnitIdentify

interface UnitInfo<T: Any> {
  companion object {
    @JvmStatic
    fun <T: Any> create(identify: UnitIdentify<T>, getter: UnitGetter): UnitInfo<T> {
      return UnitInfoImpl(identify)
    }
  }
  
  fun init() {
    getter.init(identify)
  }
  
  val identify: UnitIdentify<T>
  val getter: UnitGetter
}