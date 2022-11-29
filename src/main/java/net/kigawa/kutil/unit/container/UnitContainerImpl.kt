package net.kigawa.kutil.unit.container

import net.kigawa.kutil.unit.*
import net.kigawa.kutil.unit.classlist.ClassList
import net.kigawa.kutil.unit.closer.AutoCloseAbleCloser
import net.kigawa.kutil.unit.closer.UnitCloser
import net.kigawa.kutil.unit.concurrent.ConcurrentUnitList
import net.kigawa.kutil.unit.container.*
import net.kigawa.kutil.unit.dependency.DependencyDatabase
import net.kigawa.kutil.unit.dependency.DependencyDatabaseImpl
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
  
  private val factories = ConcurrentUnitList<UnitFactory>(this)
  private val closers = ConcurrentUnitList<UnitCloser>(this)
  override var timeoutSec: Long = 10
  override var dependencyDatabase: DependencyDatabase = DependencyDatabaseImpl()
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
    val unitIdentify = UnitIdentify(unitClass, name)
    if (dependencyDatabase.contain(unitIdentify)) return mutableListOf()
    val info = UnitInfo(unitIdentify)
    
    val errors = mutableListOf<Throwable>()
    val factory = factories.last {
      try {
        it.isValid(unitIdentify)
      } catch (e: Throwable) {
        errors.add(e)
        false
      }
    } ?: return errors
    info.factory = factory
    
    try {
      dependencyDatabase.register(info, factory.dependencies(unitIdentify))
    } catch (e: Throwable) {
      errors.add(e)
    }
    
    return errors
  }
  
  override fun addFactory(unitFactory: UnitFactory, name: String?) {
    factories.add(unitFactory, name)
  }
  
  override fun removeFactory(factoryClass: Class<out UnitFactory>, name: String?) {
    factories.remove(factoryClass, name)
  }
  
  override fun addUnit(unit: Any, name: String?) {
    val unitIdentify = UnitIdentify(unit.javaClass, name)
    val unitInfo = UnitInfo(unitIdentify)
    unitInfo.unit = unit
    dependencyDatabase.register(unitInfo, mutableListOf())
  }
  
  override fun removeUnitAsync(unitClass: Class<*>, name: String?): FutureTask<MutableList<Throwable>> {
    val future = FutureTask {
      val errors = mutableListOf<Throwable>()
      getUnitList(unitClass, name).forEach {unit->
        if (unit is UnitContainerImpl) return@forEach
        closers.filter {
          return@filter try {
            it.isValid(unit)
          } catch (e: Throwable) {
            errors.add(e)
            false
          }
        }.map {
          val future = FutureTask {it.closeUnit(unit)}
          executor.run {future.run()}
          future
        }.forEach {
          try {
            it.get()
          } catch (e: Throwable) {
            errors.add(e)
          }
        }
      }
      errors
    }
    executor.run {future.run()}
    return future
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
    list.addAll(dependencyDatabase.identifyList())
    parent?.let {list.addAll(it.getIdentifies())}
    return list
  }
  
  private fun initUnit(unitInfo: UnitInfo): FutureTask<Unit>? {
    val future = synchronized(unitInfo) {
      if (unitInfo.status == UnitStatus.INITIALIZED) return null
      if (unitInfo.status == UnitStatus.INITIALIZING) return null
      if (unitInfo.status != UnitStatus.LOADED) return null
      
      val factory = unitInfo.factory!!
      
      val future = FutureTask {
        unitInfo.unit = factory.init(unitInfo.unitIdentify, this)
      }
      unitInfo.future = future
      future
    }
    executor.run(future::run)
    return future
  }
  
  override fun <T> initUnitsAsync(unitClass: Class<T>, name: String?): FutureTask<MutableList<Throwable>> {
    val unitIdentify = UnitIdentify(unitClass, name)
    val future = FutureTask {
      val errors = mutableListOf<Throwable>()
      dependencyDatabase.findInfo(unitIdentify).map {initUnit(it)}.forEach {
        try {
          it?.get(timeoutSec + 1, TimeUnit.SECONDS)
        } catch (e: TimeoutException) {
          errors.add(RuntimeUnitException(unitClass, name, "could not init unit", e))
        } catch (e: ExecutionException) {
          errors.add(RuntimeUnitException(unitClass, name, "could not init unit", e.cause))
        }
      }
      errors
    }
    executor.run {future.run()}
    return future
  }
  
  override fun close() {
    removeUnit(Any::class.java).forEach {it.printStackTrace()}
  }
  
  @Suppress("UNCHECKED_CAST")
  override fun <T> getUnitList(unitClass: Class<T>, name: String?): List<T> {
    val identify = UnitIdentify(unitClass, name)
    val unitInfoList = dependencyDatabase.findInfo(identify)
    
    val units = mutableListOf<T>()
    
    for (info in unitInfoList) {
      units.add(info.useStatus {
        if (it == UnitStatus.INITIALIZED) {
          return@useStatus info.unit as T
        }
        throw UnitNotInitException(info, "unit is not initialized\n\tstatus: $it")
      })
    }
    parent?.getUnitList(unitClass)?.let {units.addAll(it)}
    return units
  }
  
  override fun <T> contain(unitClass: Class<T>, name: String?): Boolean {
    return dependencyDatabase.findInfo(UnitIdentify(unitClass, name)).isNotEmpty()
  }
}