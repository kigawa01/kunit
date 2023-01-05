package net.kigawa.kutil.unit.component

import net.kigawa.kutil.unit.annotation.getter.LateInit
import net.kigawa.kutil.unit.api.component.UnitInfo
import net.kigawa.kutil.unit.api.extention.UnitGetter

@LateInit
class UnitInfoImpl<T: Any>(
  identify: UnitIdentify<T>,
  override val getter: UnitGetter,
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