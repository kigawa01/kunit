package net.kigawa.kutil.unit.container

import net.kigawa.kutil.unit.*
import net.kigawa.kutil.unit.classlist.ClassList
import net.kigawa.kutil.unit.closer.AutoCloseAbleCloser
import net.kigawa.kutil.unit.closer.UnitCloser
import net.kigawa.kutil.unit.concurrent.ConcurrentList
import net.kigawa.kutil.unit.concurrent.UnitsList
import net.kigawa.kutil.unit.container.*
import net.kigawa.kutil.unit.exception.RuntimeUnitException
import net.kigawa.kutil.unit.exception.UnitNotInitException
import net.kigawa.kutil.unit.factory.DefaultFactory
import net.kigawa.kutil.unit.factory.UnitFactory
import java.util.*
import java.util.concurrent.*

class UnitContainerImpl(
    private val parent: UnitContainer? = null,
    vararg units: Any,
): UnitContainer {
    constructor(vararg units: Any): this(null, *units)
    
    private val infoList = UnitsList()
    private val factories = ConcurrentList<UnitFactory>()
    private val closers = ConcurrentList<UnitCloser>()
    override var timeoutSec: Long = 100
    override fun addCloser(closer: UnitCloser) {
        closers.add(closer)
        addUnit(closer)
    }
    
    override fun removeCloser(closerClass: Class<out UnitCloser>) {
        closers.remove(closerClass)
        removeUnit(closerClass)
    }
    
    init {
        addUnit(this, null)
        addFactory(DefaultFactory())
        addCloser(AutoCloseAbleCloser())
        Runtime.getRuntime().addShutdownHook(Thread {close()})
        units.forEach {addUnit(it)}
    }
    
    override var executor: (Runnable)->Any = {it.run()}
    
    override fun registerUnit(unitClass: Class<*>, name: String?) {
        if (infoList.contain(unitClass, name)) return
        val unitInfo = UnitInfo(unitClass, name)
        try {
            val factory = factories.last {it.isValid(unitClass)}
            unitInfo.factory = factory
            infoList.put(unitInfo)
        } catch (_: NoSuchElementException) {
        }
    }
    
    override fun addFactory(unitFactory: UnitFactory) {
        factories.add(unitFactory)
        addUnit(unitFactory)
    }
    
    override fun removeFactory(factoryClass: Class<out UnitFactory>) {
        factories.remove(factoryClass)
        removeUnit(factoryClass)
    }
    
    override fun addUnit(unit: Any, name: String?) {
        val unitInfo = UnitInfo(unit.javaClass, name)
        unitInfo.unit = unit
        infoList.put(unitInfo)
    }
    
    override fun removeUnit(unitClass: Class<*>, name: String?): MutableList<Throwable> {
        val errors = mutableListOf<Throwable>()
        getUnitList(unitClass, name).forEach {unit->
            if (unit is UnitContainerImpl) return@forEach
            val closers = closers.filter {
                return@filter try {
                    it.isValid(unit)
                } catch (e: Throwable) {
                    errors.add(e)
                    false
                }
            }
            
            val futures = mutableListOf<Future<*>>()
            closers.forEach {
                val future = FutureTask {it.closeUnit(unit)}
                futures.add(future)
                executor.run {
                    future.run()
                }
            }
            
            futures.forEach {
                try {
                    it.get()
                } catch (e: Throwable) {
                    errors.add(e)
                }
            }
        }
        return errors
    }
    
    override fun registerUnits(classList: ClassList): MutableList<Throwable> {
        val errors = mutableListOf<Throwable>()
        errors.addAll(classList.errors)

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
        list.addAll(infoList.unitKeys())
        parent?.let {list.addAll(it.getIdentifies())}
        return list
    }
    
    @Synchronized
    override fun <T> initUnits(unitClass: Class<T>, name: String?): MutableList<Throwable> {
        val errors = mutableListOf<Throwable>()
        val unitInfoList = infoList.getUnits(unitClass, name)
        
        unitInfoList.forEach {
            try {
                initUnit(it)
            } catch (e: Throwable) {
                errors.add(e)
            }
        }
        
        return errors
    }
    
    override fun close() {
        removeUnit(Any::class.java).forEach {it.printStackTrace()}
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
        val unitInfoList = infoList.getUnits(unitClass, name)
        
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
        parent?.getUnitList(unitClass)?.let {units.addAll(it)}
        return units
    }
}