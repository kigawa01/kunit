package net.kigawa.kutil.unit.container

import net.kigawa.kutil.unit.classlist.ClassList
import net.kigawa.kutil.unit.factory.UnitFactory
import net.kigawa.kutil.unit.runtimeexception.NoSingleUnitException
import java.util.*

interface UnitContainer {
    companion object {
        fun create(vararg units: Any): UnitContainer {
            return create(null, units)
        }

        fun create(parent: UnitContainerImpl?, vararg units: Any): UnitContainer {
            return UnitContainerImpl(parent, units)
        }
    }
    var executor: ((Runnable) -> Any)
    fun addFactory(unitFactory: UnitFactory)
    fun addUnit(unit: Any) {
        addUnit(unit, null)
    }

    fun addUnit(unit: Any, name: String?)
    fun registerUnits(classList: ClassList): MutableList<Throwable>
    fun registerUnit(unitClass: Class<*>, name: String?)
    fun getIdentifies(): MutableList<UnitIdentify>

    @Throws(NoSingleUnitException::class)
    fun <T> getUnit(unitClass: Class<T>): T {
        return getUnit(unitClass, null)
    }

    @Throws(NoSingleUnitException::class)
    fun <T> getUnit(unitClass: Class<T>, name: String?): T
    fun initUnits(): MutableList<Throwable> {
        return initUnits(Object::class.java)
    }

    fun <T> initUnits(unitClass: Class<T>): MutableList<Throwable> {
        return initUnits(unitClass, null)
    }

    fun <T> initUnits(unitClass: Class<T>, name: String?): MutableList<Throwable>
}