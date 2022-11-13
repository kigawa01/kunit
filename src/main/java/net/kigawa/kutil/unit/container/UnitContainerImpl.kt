package net.kigawa.kutil.unit.container

import net.kigawa.kutil.unit.classlist.ClassList
import net.kigawa.kutil.unit.exception.RuntimeUnitException
import net.kigawa.kutil.unit.exception.UnitNotInitException
import net.kigawa.kutil.unit.factory.DefaultFactory
import net.kigawa.kutil.unit.factory.UnitFactory
import java.util.*
import java.util.concurrent.ExecutionException
import java.util.concurrent.FutureTask
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class UnitContainerImpl(
    private val parent: UnitContainer? = null,
    vararg units: Any,
) : UnitContainer {
    constructor(vararg units: Any) : this(null, *units)

    private val unitInfoList = UnitsList()
    private val factories = UnitFactoriesList()
    override var timeoutSec: Long = 100

    init {
        addUnit(this, null)
        addFactory(DefaultFactory())
    }

    override var executor: (Runnable) -> Any = { it.run() }

    override fun registerUnit(unitClass: Class<*>, name: String?) {
        val unitInfo = UnitInfo(unitClass, name)
        try {
            val factory = factories.last { it.isValid(unitClass) }
            unitInfo.factory = factory
            synchronized(unitInfoList) {
                unitInfoList.put(unitInfo)
            }
        } catch (_: NoSuchElementException) {
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
                initUnit(it)
            } catch (e: Throwable) {
                errors.add(e)
            }
        }

        return errors
    }

    private fun initUnit(unitInfo: UnitInfo) {
        val future = synchronized(unitInfo) {
            if (unitInfo.status == UnitStatus.INITIALIZED) return
            if (unitInfo.status == UnitStatus.INITIALIZING) return
            if (unitInfo.status != UnitStatus.LOADED)
                throw RuntimeUnitException("unit status is not valid class: ${unitInfo.unitClass} name: ${unitInfo.name}")

            val factory = unitInfo.factory!!

            val future = FutureTask {
                factory.init(unitInfo.unitClass, this)
            }
            unitInfo.future = future
            future
        }
        executor.run(future::run)
        try {
            unitInfo.unit = future.get(timeoutSec, TimeUnit.SECONDS)
        } catch (e: TimeoutException) {
            throw RuntimeUnitException("could not init unit: ${unitInfo.unitClass}", e)
        } catch (e: ExecutionException) {
            throw RuntimeUnitException("could not init unit: ${unitInfo.unitClass}", e.cause)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> getUnitList(unitClass: Class<T>, name: String?): List<T> {
        val unitInfoList = unitInfoList.getUnits(unitClass, name)

        val units = mutableListOf<T>()
        units.addAll(unitInfoList.map {
            if (it.status == UnitStatus.INITIALIZED) return@map it.unit as T
            if (it.status == UnitStatus.FAIL) throw UnitNotInitException("unit is not initialized")
            if (it.status == UnitStatus.LOADED) initUnit(it)
            try {
                return@map it.future!!.get(timeoutSec, TimeUnit.SECONDS) as T
            } catch (e: TimeoutException) {
                throw RuntimeUnitException("could not get unit: $unitClass", e)
            } catch (e: ExecutionException) {
                throw RuntimeUnitException("could not get unit: $unitClass", e.cause)
            }
        })
        parent?.getUnitList(unitClass)?.let { units.addAll(it) }
        return units
    }
}