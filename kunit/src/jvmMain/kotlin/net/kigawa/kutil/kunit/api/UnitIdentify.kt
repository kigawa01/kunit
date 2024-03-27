package net.kigawa.kutil.kunit.api

import net.kigawa.kutil.kunit.api.annotation.ArgName
import net.kigawa.kutil.kutil.api.reflection.KutilReflect
import java.lang.reflect.Executable
import java.util.*

class UnitIdentify<T>(
  val unitClass: Class<T>,
  val name: String?,
) {
  companion object {
    @JvmStatic
    fun createList(executable: Executable): List<UnitIdentify<out Any>> {
      return executable.parameters.map {UnitIdentify(it.type, it.getAnnotation(ArgName::class.java)?.name)}
    }
  }
  
  override fun equals(other: Any?): Boolean {
    if (other !is UnitIdentify<*>) return false
    if (unitClass != other.unitClass) return false
    return equalsName(other)
  }
  
  fun equalsOrSuperClass(superClassInfo: UnitIdentify<*>): Boolean {
    if (!instanceOf(superClassInfo.unitClass)) return false
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
    return KutilReflect.instanceOf(unitClass, superClass)
  }
  
  override fun toString(): String {
    return "UnitIdentify(unitClass=$unitClass, name=$name)"
  }
}