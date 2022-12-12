package net.kigawa.kutil.unit.component.container

import net.kigawa.kutil.unit.component.closer.UnitCloserComponent
import net.kigawa.kutil.unit.component.UnitContainerConponents
import net.kigawa.kutil.unit.component.UnitContainerConponentsHolder
import net.kigawa.kutil.unit.component.database.UnitDatabase
import net.kigawa.kutil.unit.component.initializer.UnitInitializer
import net.kigawa.kutil.unit.component.initializer.UnitInitializerImpl
import net.kigawa.kutil.unit.component.register.UnitRegister
import net.kigawa.kutil.unit.component.resolver.DependencyResolver
import net.kigawa.kutil.unit.exception.NoFoundUnitException
import net.kigawa.kutil.unit.exception.NoSingleUnitException
import java.util.*
import java.util.concurrent.*

interface UnitContainer: AutoCloseable, UnitInitializer, UnitRegister, UnitContainerConponentsHolder {
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
  
  
  fun removeUnitAsync(unitClass: Class<*>, name: String?): FutureTask<MutableList<Throwable>>
  fun removeUnit(unitClass: Class<*>): MutableList<Throwable> {
    return removeUnit(unitClass, null)
  }
  
  fun removeUnit(unitClass: Class<*>, name: String?): MutableList<Throwable> {
    return try {
      initUnitsAsync(unitClass, name).get(conponents.timeoutSec, TimeUnit.SECONDS)
    } catch (e: TimeoutException) {
      val cause = e.cause
      if (cause == null) mutableListOf(e)
      else mutableListOf(cause)
    }
  }
  
  fun addUnit(unit: Any, name: String?)
  fun addUnit(unit: Any) {
    addUnit(unit, null)
  }
  
  fun getIdentifies(): MutableList<UnitIdentify<*>>
  
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
    var units = getUnitList(unitClass, name)
    if (units.isEmpty()) {
      units = getUnitList(unitClass)
      if (units.isEmpty()) throw NoFoundUnitException(unitClass, name, "unit is not found")
    }
    if (units.size == 1) {
      return units[0]
    }
    throw NoSingleUnitException(unitClass, name, "unit is not single count: ${units.size}")
  }
  
  fun <T> contain(unitClass: Class<T>, name: String?): Boolean
}