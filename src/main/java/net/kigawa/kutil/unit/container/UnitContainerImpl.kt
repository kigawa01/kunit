package net.kigawa.kutil.unit.container

import net.kigawa.kutil.unit.*
import net.kigawa.kutil.unit.classlist.ClassList
import net.kigawa.kutil.unit.closer.AutoCloseAbleCloser
import net.kigawa.kutil.unit.closer.UnitCloser
import net.kigawa.kutil.unit.concurrent.ConcurrentUnitList
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
  @Suppress("unused")
  constructor(vararg units: Any): this(null, *units)
  
  private val infoList = UnitsList()
  private val factories = ConcurrentUnitList<UnitFactory>(this)
  private val closers = ConcurrentUnitList<UnitCloser>(this)
  override var timeoutSec: Long = 100
  override var executor: (Runnable)->Any = {it.run()}
  
  init {
    addUnit(this, null)
    addFactory(DefaultFactory())
    addCloser(AutoCloseAbleCloser())
    Runtime.getRuntime().addShutdownHook(Thread {close()})
    units.forEach {addUnit(it)}
  }
  
  override fun addCloser(closer: UnitCloser, name: String?) {
    closers.add(closer, name)
  }
  
  override fun removeCloser(closerClass: Class<out UnitCloser>, name: String?): MutableList<Throwable> {
    return closers.remove(closerClass, name)
  }
  
  override fun registerUnit(unitClass: Class<*>, name: String?): MutableList<Throwable> {
    val unitInfo = UnitInfo(unitClass, name)
    if (infoList.contain(unitInfo)) return mutableListOf()
    
    val errors = mutableListOf<Throwable>()
    val factory = factories.last {
      try {
        it.isValid(unitClass)
      } catch (e: Throwable) {
        errors.add(e)
        false
      }
    } ?: return errors
    unitInfo.factory = factory
    infoList.put(unitInfo)
    return errors
  }
  
  override fun addFactory(unitFactory: UnitFactory, name: String?) {
    factories.add(unitFactory, name)
  }
  
  override fun removeFactory(factoryClass: Class<out UnitFactory>, name: String?) {
    factories.remove(factoryClass, name)
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
  
  override fun <T> initUnitsAsync(unitClass: Class<T>, name: String?): MutableList<Throwable> {
    val errors = mutableListOf<Throwable>()
    
    initUnits(unitClass, name).forEach {
      try {
        it?.get(timeoutSec, TimeUnit.SECONDS)
      } catch (e: TimeoutException) {
        throw RuntimeUnitException(unitClass, name, "could not init unit", e)
      } catch (e: ExecutionException) {
        throw RuntimeUnitException(unitClass, name, "could not init unit", e.cause)
      }
    }
    
    return errors
  }
  
  override fun close() {
    removeUnit(Any::class.java).forEach {it.printStackTrace()}
  }
  
  private fun initUnit(unitInfo: UnitInfo): FutureTask<Unit>? {
    val future = synchronized(unitInfo) {
      if (unitInfo.status == UnitStatus.INITIALIZED) return null
      if (unitInfo.status == UnitStatus.INITIALIZING) return null
      if (unitInfo.status != UnitStatus.LOADED) return null
      
      val factory = unitInfo.factory!!
      
      val future = FutureTask {
        unitInfo.unit = factory.init(unitInfo.unitClass, this)
      }
      unitInfo.future = future
      future
    }
    executor.run(future::run)
    return future
  }
  
  @Suppress("UNCHECKED_CAST")
  override fun <T> getUnitList(unitClass: Class<T>, name: String?): List<T> {
    val unitInfoList = infoList.getUnits(unitClass, name)
    
    val units = mutableListOf<T>()
    units.addAll(unitInfoList.map {
      if (it.status == UnitStatus.INITIALIZED) return@map it.unit as T
      if (it.status == UnitStatus.FAIL) throw UnitNotInitException(it, "unit is not initialized")
      if (it.status == UnitStatus.LOADED) initUnit(it)
      try {
        return@map it.future!!.get(timeoutSec, TimeUnit.SECONDS) as T
      } catch (e: TimeoutException) {
        throw RuntimeUnitException(unitClass, name, "could not get unit", e)
      } catch (e: ExecutionException) {
        throw RuntimeUnitException(unitClass, name, "could not get unit", e.cause)
      }
    })
    parent?.getUnitList(unitClass)?.let {units.addAll(it)}
    return units
  }
  
  override fun <T> contain(unitClass: Class<T>, name: String?): Boolean {
    return infoList.getUnits(unitClass, name).isNotEmpty()
  }
  
  override fun <T> initUnits(unitClass: Class<T>, name: String?): List<FutureTask<Unit>?> {
    val unitInfoList = infoList.getUnits(unitClass, name)
    return unitInfoList.map {initUnit(it)}
  }
}