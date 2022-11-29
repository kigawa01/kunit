package net.kigawa.kutil.unit

import java.util.*

class UnitIdentify(
  val unitClass: Class<*>,
  val name: String?,
) {
  override fun equals(other: Any?): Boolean {
    if (other !is UnitIdentify) return false
    if (javaClass != other.javaClass) return false
    
    if (unitClass != other.unitClass) return false
    if (name == null || other.name == null) return true
    if (name == "" || other.name == "") return true
    
    return name == other.name
  }
  
  override fun hashCode(): Int {
    return Objects.hash(unitClass, name)
  }
}