package net.kigawa.kutil.unit.component

import net.kigawa.kutil.unit.util.Util
import java.util.*

class UnitIdentify<T>(
  val unitClass: Class<T>,
  val name: String?,
) {
  override fun equals(other: Any?): Boolean {
    if (other !is UnitIdentify<*>) return false
    if (javaClass != other.javaClass) return false
    return equalsName(other)
  }
  
  fun equalsOrSuperClass(superClassInfo: UnitIdentify<*>): Boolean {
    if (!Util.instanceOf(unitClass, superClassInfo.unitClass)) return false
    return equalsName(superClassInfo)
  }
  
  fun equalsName(other: UnitIdentify<*>): Boolean {
    if (name == null || other.name == null) return true
    if (name == "" || other.name == "") return true
    
    return name == other.name
  }
  
  override fun hashCode(): Int {
    return Objects.hash(unitClass, name)
  }
  
  fun instanceOf(superClass: Class<out Any>): Boolean {
    return Util.instanceOf(unitClass, superClass)
  }
}