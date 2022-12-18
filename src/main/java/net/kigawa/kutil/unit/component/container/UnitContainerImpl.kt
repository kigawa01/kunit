package net.kigawa.kutil.unit.component.container

import net.kigawa.kutil.unit.*
import net.kigawa.kutil.unit.component.*
import net.kigawa.kutil.unit.component.database.*
import net.kigawa.kutil.unit.component.info.UnitInfo
import net.kigawa.kutil.unit.concurrent.UnitClassList
import net.kigawa.kutil.unit.container.*
import net.kigawa.kutil.unit.exception.*
import net.kigawa.kutil.unit.extension.identify.UnitIdentifies
import net.kigawa.kutil.unit.extension.closer.AutoCloseAbleCloser
import net.kigawa.kutil.unit.extension.closer.UnitCloser
import net.kigawa.kutil.unit.extension.factory.NormalFactory
import net.kigawa.kutil.unit.extension.factory.UnitFactory
import net.kigawa.kutil.unit.extension.identify.UnitIdentify
import java.util.*
import java.util.concurrent.*

class UnitContainerImpl(
  private val parent: UnitContainer? = null,
  vararg units: Any,
): UnitContainer {
  private val factories = UnitClassList<UnitFactory>(this)
  private val closers = UnitClassList<UnitCloser>(this)
  override var conponents: UnitContainerConponents = UnitContainerConponentsImpl(this)
  
  init {
    addUnit(this, null)
    addFactory(NormalFactory())
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
    unitDatabase.register(unitInfo)
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
  
  override fun getIdentifies(): MutableList<UnitIdentify> {
    val list = mutableListOf<UnitIdentify>()
    list.addAll(unitDatabase.identifyList())
    parent?.let {list.addAll(it.getIdentifies())}
    return list
  }
  
  override fun <T> initUnitsAsync(unitClass: Class<T>, name: String?): FutureTask<MutableList<Throwable>> {
    
    return future
  }
  
  override fun registerUnits(classList: UnitIdentifies): MutableList<Throwable> {
    TODO("Not yet implemented")
  }
  
  override fun registerUnit(unitClass: Class<*>, name: String?): MutableList<Throwable> {
    TODO("Not yet implemented")
  }
  
  override fun close() {
    removeUnit(Any::class.java).forEach {it.printStackTrace()}
  }
  
  override fun <T> initUnits(unitClass: Class<T>, name: String?): MutableList<Throwable> {
    TODO("Not yet implemented")
  }
  
  @Suppress("UNCHECKED_CAST")
  override fun <T> getUnitList(unitClass: Class<T>, name: String?): List<T> {
    val identify = UnitIdentify(unitClass, name)
    val units = unitDatabase.findInfo(identify).map {
      it.getUnit() as T
    }.toMutableList()
    parent?.getUnitList(unitClass)?.let {units.addAll(it)}
    
    return units
  }
  
  override fun <T> contain(unitClass: Class<T>, name: String?): Boolean {
    return unitDatabase.findInfo(UnitIdentify(unitClass, name)).isNotEmpty()
  }
}