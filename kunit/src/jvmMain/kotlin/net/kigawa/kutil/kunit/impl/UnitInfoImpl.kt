package net.kigawa.kutil.kunit.impl

import net.kigawa.kutil.kunit.api.UnitIdentify
import net.kigawa.kutil.kunit.api.annotation.getter.LateInit
import net.kigawa.kutil.kunit.api.component.UnitInfo
import net.kigawa.kutil.kunit.api.extention.UnitStore

@LateInit
class UnitInfoImpl<T: Any>(
  identify: UnitIdentify<T>,
  override val getter: UnitStore,
): UnitInfo<T> {
  override val identify: UnitIdentify<T>
  
  init {
    val name = if (identify.name == null || identify.name == "")
      identify.unitClass.name
    else identify.name
    
    this.identify = UnitIdentify(identify.unitClass, name)
  }
  
  override fun toString(): String {
    return "UnitInfoImpl(getter=$getter," +
           "\n\t identify=$identify)"
  }
}