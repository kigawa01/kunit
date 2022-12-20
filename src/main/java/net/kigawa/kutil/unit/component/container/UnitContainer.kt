package net.kigawa.kutil.unit.component.container

import net.kigawa.kutil.unit.exception.*
import net.kigawa.kutil.unit.extension.identify.UnitIdentify
import java.util.*
import java.util.concurrent.*

interface UnitContainer: AutoCloseable {
  companion object {
    @JvmStatic
    fun create(): UnitContainer {
      return create(null)
    }
    
    @JvmStatic
    fun create(parent: UnitContainerImpl?): UnitContainer {
      return UnitContainerImpl(parent)
    }
  }
  
  fun removeUnit(unitClass: Class<out Any>) {
    removeUnit(unitClass, null)
  }
  
  fun removeUnit(unitClass: Class<out Any>, name: String?) {
    removeUnit(UnitIdentify(unitClass, name))
  }
  
  fun removeUnit(identify: UnitIdentify<out Any>)
  
  @Throws(NoSingleUnitException::class)
  fun <T: Any> getUnitList(identify: UnitIdentify<T>): List<T>
  
  @Throws(NoSingleUnitException::class)
  fun <T: Any> getUnitList(unitClass: Class<T>): List<T> {
    return getUnitList(unitClass, null)
  }
  
  @Throws(NoSingleUnitException::class)
  fun <T: Any> getUnitList(unitClass: Class<T>, name: String?): List<T> {
    return getUnitList(UnitIdentify(unitClass, name))
  }
  
  @Throws(NoSingleUnitException::class)
  fun <T: Any> getUnit(unitClass: Class<T>): T {
    return getUnit(unitClass, null)
  }
  
  @Throws(NoSingleUnitException::class)
  fun <T: Any> getUnit(unitClass: Class<T>, name: String?): T {
    return getUnit(UnitIdentify(unitClass, name))
  }
  
  
  @Throws(NoSingleUnitException::class)
  fun <T: Any> getUnit(identify: UnitIdentify<T>): T {
    var units = getUnitList(identify)
    if (units.isEmpty()) {
      units = getUnitList(identify.unitClass)
      if (units.isEmpty()) throw UnitException("unit is not found", identify)
    }
    if (units.size == 1) {
      return units[0]
    }
    throw UnitException("unit is not single count: ${units.size}", identify)
  }
  
  fun <T: Any> contain(identify: UnitIdentify<T>): Boolean {
    return getUnitList(identify).isNotEmpty()
  }
}