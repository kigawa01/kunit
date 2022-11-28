package net.kigawa.kutil.unit.container

import net.kigawa.kutil.unit.UnitIdentify
import net.kigawa.kutil.unit.classlist.ClassList
import net.kigawa.kutil.unit.closer.UnitCloser
import net.kigawa.kutil.unit.exception.NoFoundUnitException
import net.kigawa.kutil.unit.exception.NoSingleUnitException
import net.kigawa.kutil.unit.factory.UnitFactory
import java.util.*
import java.util.concurrent.FutureTask

interface UnitContainer: AutoCloseable {
  companion object {
    @JvmStatic
    fun create(vararg units: Any): UnitContainer {
      return create(null, *units)
    }
    
    @JvmStatic
    fun create(parent: UnitContainerImpl?, vararg units: Any): UnitContainer {
      return UnitContainerImpl(parent, *units)
    }
  }
  
  var executor: ((Runnable)->Any)
  var timeoutSec: Long
  fun addCloser(closer: UnitCloser, name: String?)
  fun addCloser(closer: UnitCloser) {
    addCloser(closer, null)
  }
  
  fun removeCloser(closerClass: Class<out UnitCloser>, name: String?): MutableList<Throwable>
  
  @Suppress("unused")
  fun removeCloser(closerClass: Class<out UnitCloser>): MutableList<Throwable> {
    return removeCloser(closerClass, null)
  }
  
  fun addFactory(unitFactory: UnitFactory, name: String?)
  fun addFactory(unitFactory: UnitFactory) {
    addFactory(unitFactory, null)
  }
  
  fun removeFactory(factoryClass: Class<out UnitFactory>, name: String?)
  
  @Suppress("unused")
  fun removeFactory(factoryClass: Class<out UnitFactory>) {
    removeFactory(factoryClass, null)
  }
  
  fun removeUnit(unitClass: Class<*>, name: String?): MutableList<Throwable>
  fun removeUnit(unitClass: Class<*>): MutableList<Throwable> {
    return removeUnit(unitClass, null)
  }
  
  fun addUnit(unit: Any, name: String?)
  fun addUnit(unit: Any) {
    addUnit(unit, null)
  }
  
  fun registerUnits(classList: ClassList): MutableList<Throwable>
  fun registerUnit(unitClass: Class<*>, name: String?): MutableList<Throwable>
  fun registerUnit(unitClass: Class<*>): MutableList<Throwable> {
    return registerUnit(unitClass, null)
  }
  
  fun getIdentifies(): MutableList<UnitIdentify>
  
  @Throws(NoSingleUnitException::class)
  fun <T> getUnitList(unitClass: Class<T>, name: String?): List<T>
  
  @Throws(NoSingleUnitException::class)
  fun <T> getUnitList(unitClass: Class<T>): List<T> {
    return getUnitList(unitClass, null)
  }
  
  @Throws(NoSingleUnitException::class)
  fun <T> getUnit(unitClass: Class<T>): T {
    return getUnit(unitClass, null)
  }
  
  
  @Throws(NoSingleUnitException::class)
  fun <T> getUnit(unitClass: Class<T>, name: String?): T {
    val units = getUnitList(unitClass, name)
    if (units.isEmpty())
      throw NoFoundUnitException(unitClass, name, "unit is not found")
    if (units.size == 1) {
      return units[0]
    }
    throw NoSingleUnitException(unitClass, name, "unit is not single count: ${units.size}")
  }
  
  fun <T> contain(unitClass: Class<T>, name: String?): Boolean
  
  fun initUnits(): List<FutureTask<Unit>?> {
    return initUnits(Object::class.java)
  }
  
  fun <T> initUnits(unitClass: Class<T>): List<FutureTask<Unit>?> {
    return initUnits(unitClass, null)
  }
  
  
  fun <T> initUnits(unitClass: Class<T>, name: String?): List<FutureTask<Unit>?>
  fun <T> initUnitsAsync(unitClass: Class<T>, name: String?): MutableList<Throwable>
}