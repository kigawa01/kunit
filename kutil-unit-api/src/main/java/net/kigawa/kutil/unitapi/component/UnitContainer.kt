@file:Suppress("unused")

package net.kigawa.kutil.unitapi.component

import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.exception.*
import java.util.*
import java.util.concurrent.*

interface UnitContainer: AutoCloseable {
  companion object {
    @JvmStatic
    var implementsClass: Class<out UnitContainer>? = null
    
    init {
      try {
        @Suppress("UNCHECKED_CAST")
        implementsClass = Class.forName("net.kigawa.kutil.unit.component.UnitContainerImpl") as Class<out UnitContainer>
      } catch (_: ClassNotFoundException) {
      }
    }
    
    @JvmStatic
    fun create() = create(null)
    
    @JvmStatic
    fun create(parent: UnitContainer?): UnitContainer {
      val constructor = implementsClass?.getConstructor(UnitContainer::class.java)
                        ?: throw UnitException("container class is not found")
      return constructor.newInstance(parent)
    }
  }
  
  fun removeUnit(unitClass: Class<out Any>) {
    removeUnit(unitClass, null)
  }
  
  fun removeUnit(unitClass: Class<out Any>, name: String?) {
    removeUnit(UnitIdentify(unitClass, name))
  }
  
  fun removeUnit(identify: UnitIdentify<out Any>)
  
  fun <T: Any> getUnitList(identify: UnitIdentify<T>): List<T>
  
  fun <T: Any> getUnitList(unitClass: Class<T>): List<T> {
    return getUnitList(unitClass, null)
  }
  
  fun <T: Any> getUnitList(unitClass: Class<T>, name: String?): List<T> {
    return getUnitList(UnitIdentify(unitClass, name))
  }
  
  fun <T: Any> getUnit(unitClass: Class<T>): T {
    return getUnit(unitClass, null)
  }
  
  fun <T: Any> getUnit(unitClass: Class<T>, name: String?): T {
    return getUnit(UnitIdentify(unitClass, name))
  }
  
  fun <T: Any> getUnit(identify: UnitIdentify<T>): T {
    var units = getUnitList(identify)
    if (units.isEmpty()) {
      units = getUnitList(identify.unitClass)
      if (units.isEmpty())
        throw NoFoundUnitException("unit is not found", identify)
    }
    if (units.size == 1) {
      return units[0]
    }
    throw NoSingleUnitException("unit is not single count: ${units.size}", identify)
  }
  
  fun <T: Any> contain(identify: UnitIdentify<T>): Boolean {
    return getUnitList(identify).isNotEmpty()
  }
}