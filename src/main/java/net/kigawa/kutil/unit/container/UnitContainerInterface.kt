package net.kigawa.kutil.unit.container

import net.kigawa.kutil.unit.classlist.ClassList
import net.kigawa.kutil.unit.factory.UnitFactory
import net.kigawa.kutil.unit.runtimeexception.NoSingleUnitException
import java.util.*

interface UnitContainerInterface {
    companion object {
        fun create(vararg units: Any): UnitContainer {
            return create(null, units)
        }

        fun create(parent: UnitContainer?, vararg units: Any): UnitContainer {
            return UnitContainer(parent, units)
        }
    }

    fun addFactory(unitFactory: UnitFactory)
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
        return initUnits(Objects::class.java)
    }

    fun <T> initUnits(unitClass: Class<T>): MutableList<Throwable> {
        return initUnits(unitClass, null)
    }

    fun <T> initUnits(unitClass: Class<T>, name: String?): MutableList<Throwable>
}