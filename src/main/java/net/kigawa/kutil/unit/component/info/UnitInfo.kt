package net.kigawa.kutil.unit.component.info

import net.kigawa.kutil.unit.component.UnitIdentify
import net.kigawa.kutil.unit.extension.getter.UnitGetter

interface UnitInfo<T: Any> {
  companion object {
    @JvmStatic
    fun <T: Any> create(identify: UnitIdentify<T>, getter: UnitGetter): UnitInfo<T> {
      return UnitInfoImpl(identify, getter)
    }
  }
  
  val identify: UnitIdentify<T>
  val getter: UnitGetter
  fun get(): T {
    return getter.get(identify)
  }
  
  fun instanceOf(superClass: Class<out Any>): Boolean {
    return identify.instanceOf(superClass)
  }
}