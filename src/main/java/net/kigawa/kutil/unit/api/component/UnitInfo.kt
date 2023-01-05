package net.kigawa.kutil.unit.api.component

import net.kigawa.kutil.unit.component.*
import net.kigawa.kutil.unit.extension.getter.UnitGetter
import java.util.concurrent.Future

interface UnitInfo<T: Any> {
  companion object {
    @JvmStatic
    fun <T: Any> create(identify: UnitIdentify<T>, getter: UnitGetter): UnitInfo<T> {
      return UnitInfoImpl(identify, getter)
    }
  }
  
  val identify: UnitIdentify<T>
  val getter: UnitGetter
  fun initGetter(initStack: InitStack) {
    getter.initGetter(identify, initStack)
  }
  
  fun initOrGet(initStack: InitStack): Future<T> {
    return getter.initOrGet(identify, initStack)
  }
  
  fun get(): T {
    return getter.get(identify)
  }
  
  fun instanceOf(superClass: Class<out Any>): Boolean {
    return identify.instanceOf(superClass)
  }
}