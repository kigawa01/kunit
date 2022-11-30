package net.kigawa.kutil.unit.container

import net.kigawa.kutil.unit.*
import net.kigawa.kutil.unit.classlist.ClassList
import net.kigawa.kutil.unit.closer.AutoCloseAbleCloser
import net.kigawa.kutil.unit.closer.UnitCloser
import net.kigawa.kutil.unit.concurrent.ConcurrentUnitList
import net.kigawa.kutil.unit.container.*
import net.kigawa.kutil.unit.dependency.DependencyDatabase
import net.kigawa.kutil.unit.dependency.DependencyDatabaseImpl
import net.kigawa.kutil.unit.exception.*
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
    info.loaded(factory)
    
    try {
      dependencyDatabase.register(info)
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
    unitInfo.initialized(unit)
    dependencyDatabase.register(unitInfo)
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
  
  override fun <T> initUnitsAsync(unitClass: Class<T>, name: String?): FutureTask<MutableList<Throwable>> {
    val unitIdentify = UnitIdentify(unitClass, name)
    val future = FutureTask {
      val errors = mutableListOf<Throwable>()
      dependencyDatabase.findInfo(unitIdentify).map {
        val future = FutureTask {
          try {
            initTask(it)
          } catch (e: Throwable) {
            errors.add(e)
            it.fail()
            null
          }
        }
        executor {future.run()}
        return@map future
      }.forEach {
        try {
          it.get(timeoutSec + 1, TimeUnit.SECONDS)
        } catch (e: TimeoutException) {
          errors.add(RuntimeUnitException(unitClass, name, "could not init unit", e))
        } catch (e: ExecutionException) {
          errors.add(RuntimeUnitException(unitClass, name, "could not init unit", e.cause))
        }
      }
      return@FutureTask errors
    }
    executor.run {future.run()}
    return future
  }
  
  private fun initTask(info: UnitInfo): MutableList<Throwable> {
    val errors = mutableListOf<Throwable>()
    synchronized(info) {
      if (info.status == UnitStatus.INITIALIZED || info.status == UnitStatus.INITIALIZING || info.status == UnitStatus.FAIL)
        return errors
      if (info.status != UnitStatus.LOADED) {
        errors.add(RuntimeUnitException(info, "could not init unit\n\tstatus: ${info.status}"))
        return errors
      }
      info.initializing()
    }
    val factory = info.getFactory()
    val dependencyInfo = factory.dependencies(info.unitIdentify).map {
      var dependencyInfoList = dependencyDatabase.findInfo(it)
      if (dependencyInfoList.isEmpty()) dependencyInfoList =
        dependencyDatabase.findInfo(UnitIdentify(it.unitClass, null))
      if (dependencyInfoList.isEmpty()) {
        errors.add(NoFoundUnitException(info, "unit dependency not found"))
        return errors
      }
      if (dependencyInfoList.size != 1) {
        errors.add(NoSingleUnitException(info, "unit dependency found no single"))
        return errors
      }
      return@map dependencyInfoList[0]
    }
    dependencyInfo.map {
      if (it.status == UnitStatus.LOADED) initUnitsAsync(it.unitIdentify.unitClass, it.unitIdentify.name)
      else null
    }.forEach {
      try {
        it?.let {errors.addAll(it.get())}
      } catch (e: Throwable) {
        errors.add(e)
        return errors
      }
    }
    
    val dependencies = dependencyInfo.map {
      synchronized(it) {
        if (it.status == UnitStatus.INITIALIZING) it.initializedBlock(timeoutSec, TimeUnit.SECONDS)
        return@map it.getUnit()
      }
    }
    info.initialized(factory.init(info.unitIdentify, dependencies))
    return errors
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
      units.add(info.getUnit() as T)
    }
    parent?.getUnitList(unitClass)?.let {units.addAll(it)}
    return units
  }
  
  override fun <T> contain(unitClass: Class<T>, name: String?): Boolean {
    return dependencyDatabase.findInfo(UnitIdentify(unitClass, name)).isNotEmpty()
  }
}