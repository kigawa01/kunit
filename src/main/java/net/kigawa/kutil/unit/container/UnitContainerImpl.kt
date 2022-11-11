package net.kigawa.kutil.unit.container

import net.kigawa.kutil.unit.classlist.ClassList
import net.kigawa.kutil.unit.factory.DefaultFactory
import net.kigawa.kutil.unit.factory.UnitFactory
import net.kigawa.kutil.unit.runtimeexception.NoFoundUnitException
import net.kigawa.kutil.unit.runtimeexception.NoSingleUnitException
import net.kigawa.kutil.unit.runtimeexception.RuntimeUnitException
import net.kigawa.kutil.unit.runtimeexception.UnitNotInitException
import java.util.*
import java.util.concurrent.FutureTask

class UnitContainerImpl(
    private val parent: UnitContainer? = null,
    vararg units: Any,
) : UnitContainer {
    constructor(vararg units: Any) : this(null, *units)

    private val unitInfoList = UnitsList()
    private val factories = UnitFactoriesList()

    init {
        addFactory(DefaultFactory())
    }

    override fun registerUnit(unitClass: Class<*>, name: String?) {
        val unitInfo = UnitInfo(unitClass, name)
        try {
            val factory = factories.last { it.isValid(unitClass) }
            unitInfo.factory = factory
            synchronized(unitInfoList) {
                unitInfoList.put(unitInfo)
            }
        } catch (e: NoSuchElementException) {
            throw RuntimeUnitException("$unitClass can not register")
        }
    }

    override fun addFactory(unitFactory: UnitFactory) {
        factories.add(unitFactory)
    }

    override fun addUnit(unit: Any, name: String?) {
        val unitInfo = UnitInfo(unit.javaClass, name)
        unitInfo.unit = unit
        unitInfoList.put(unitInfo)
    }

    override fun registerUnits(classList: ClassList): MutableList<Throwable> {
        val errors = mutableListOf<Throwable>()

        classList.classes.forEach {
            try {
                registerUnit(it, null)
            } catch (e: Throwable) {
                errors.add(e)
            }
        }

        return errors
    }

    override fun getIdentifies(): MutableList<UnitIdentify> {
        val list = mutableListOf<UnitIdentify>()
        list.addAll(unitInfoList.unitKeys())
        parent?.let { list.addAll(it.getIdentifies()) }
        return list
    }

    @Synchronized
    override fun <T> initUnits(unitClass: Class<T>, name: String?): MutableList<Throwable> {
        val errors = mutableListOf<Throwable>()
        val unitInfoList = unitInfoList.getUnits(unitClass, name)

        unitInfoList.forEach {
            try {
                preInitUnit(it)
            } catch (e: Throwable) {
                errors.add(e)
            }
        }
        unitInfoList.forEach {
            try {
                initUnit(it)
            } catch (e: Throwable) {
                errors.add(e)
            }
        }

        return errors
    }

    private fun initUnit(unitInfo: UnitInfo) {
        synchronized(unitInfo) {
            if (unitInfo.status == UnitStatus.INITIALIZED) return
            if (unitInfo.status != UnitStatus.INITIALIZING)
                throw RuntimeUnitException("unit must be initializing state class: ${unitInfo.unitClass} name: ${unitInfo.name}")
            val future = unitInfo.future!!
            future.run()
            unitInfo.unit = future.get()
        }

    }

    private fun preInitUnit(unitInfo: UnitInfo) {
        synchronized(unitInfo) {
            if (unitInfo.status == UnitStatus.INITIALIZED) return
            if (unitInfo.status == UnitStatus.INITIALIZING) return
            if (unitInfo.status != UnitStatus.LOADED)
                throw RuntimeUnitException("unit status is not valid class: ${unitInfo.unitClass} name: ${unitInfo.name}")

            val factory = unitInfo.factory!!

            val future = FutureTask {
                factory.init(unitInfo.unitClass, this)
            }
            unitInfo.future = future
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> getUnit(unitClass: Class<T>, name: String?): T {
        val unitInfoList = unitInfoList.getUnits(unitClass, name)
        if (unitInfoList.isEmpty())
            throw NoFoundUnitException("unit is not found: $unitClass")
        if (unitInfoList.size == 1) {
            if (unitInfoList[0].status == UnitStatus.INITIALIZED) return unitInfoList[0].unit as T
            if (unitInfoList[0].status == UnitStatus.INITIALIZING) {
                return unitInfoList[0].future!!.get() as T
            }

            throw UnitNotInitException("unit is not initialized")
        }
        if (parent != null) return parent.getUnit(unitClass)
        throw NoSingleUnitException("unit is not single count: ${unitInfoList.size} class: $unitClass")
    }
}