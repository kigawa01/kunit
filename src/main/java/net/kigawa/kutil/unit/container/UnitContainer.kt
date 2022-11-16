package net.kigawa.kutil.unit.container

import net.kigawa.kutil.unit.UnitIdentify
import net.kigawa.kutil.unit.classlist.ClassList
import net.kigawa.kutil.unit.closer.UnitCloser
import net.kigawa.kutil.unit.exception.NoFoundUnitException
import net.kigawa.kutil.unit.exception.NoSingleUnitException
import net.kigawa.kutil.unit.factory.UnitFactory
import java.util.*

interface UnitContainer :AutoCloseable{
    companion object {
        @JvmStatic
        fun create(vararg units: Any): UnitContainer {
            return create(null, units)
        }

        @JvmStatic
        fun create(parent: UnitContainerImpl?, vararg units: Any): UnitContainer {
            return UnitContainerImpl(parent, units)
        }
    }

    var executor: ((Runnable) -> Any)
    var timeoutSec: Long
    fun addCloser(closer: UnitCloser)
    fun removeCloser(closerClass: Class<out UnitCloser>)

    fun addFactory(unitFactory: UnitFactory)
    fun removeFactory(factoryClass: Class<out UnitFactory>)

    fun addUnit(unit: Any) {
        addUnit(unit, null)
    }

    fun removeUnit(unitClass: Class<*>, name: String?): MutableList<Throwable>
    fun removeUnit(unitClass: Class<*>): MutableList<Throwable> {
        return removeUnit(unitClass, null)
    }

    fun addUnit(unit: Any, name: String?)
    fun registerUnits(classList: ClassList): MutableList<Throwable>
    fun registerUnit(unitClass: Class<*>, name: String?)
    fun getIdentifies(): MutableList<UnitIdentify>

    @Throws(NoSingleUnitException::class)
    fun <T> getUnitList(unitClass: Class<T>): List<T> {
        return getUnitList(unitClass, null)
    }

    @Throws(NoSingleUnitException::class)
    fun <T> getUnitList(unitClass: Class<T>, name: String?): List<T>

    @Throws(NoSingleUnitException::class)
    fun <T> getUnit(unitClass: Class<T>, name: String?): T {
        val units = getUnitList(unitClass, name)
        if (units.isEmpty())
            throw NoFoundUnitException("unit is not found: $unitClass")
        if (units.size == 1) {
            return units[0]
        }
        throw NoSingleUnitException("unit is not single count: ${units.size} class: $unitClass")
    }

    @Throws(NoSingleUnitException::class)
    fun <T> getUnit(unitClass: Class<T>): T {
        return getUnit(unitClass, null)
    }

    fun initUnits(): MutableList<Throwable> {
        return initUnits(Object::class.java)
    }

    fun <T> initUnits(unitClass: Class<T>): MutableList<Throwable> {
        return initUnits(unitClass, null)
    }

    fun <T> initUnits(unitClass: Class<T>, name: String?): MutableList<Throwable>
}