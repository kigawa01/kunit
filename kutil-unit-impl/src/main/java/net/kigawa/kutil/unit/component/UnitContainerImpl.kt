package net.kigawa.kutil.unit.component

import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.annotation.getter.LateInit
import net.kigawa.kutil.unitapi.component.*
import net.kigawa.kutil.unitapi.options.FindOptions

@Suppress("unused")
@LateInit
class UnitContainerImpl(
  private val parent: UnitContainer?,
): UnitContainer {
  lateinit var closerComponent: UnitCloserComponent
  lateinit var loggerComponent: UnitLoggerComponent
  lateinit var databaseComponent: UnitDatabaseComponent
  lateinit var finderComponent: UnitFinderComponent
  
  constructor(): this(null)
  
  init {
    ContainerInitializer(this)
  }
  
  override fun removeUnit(identify: UnitIdentify<out Any>) {
    databaseComponent.findByIdentify(identify).forEach {info->
      loggerComponent.catch(null) {
        removeInfo(info)
      }
    }
  }
  
  private fun removeInfo(info: UnitInfo<out Any>) {
    databaseComponent.unregisterInfo(info)
    closerComponent.closeUnit(info)
  }
  
  override fun <T: Any> getUnitList(identify: UnitIdentify<T>, findOptions: FindOptions): List<T> {
    val list = finderComponent.findUnits(identify, findOptions).toMutableList()
    parent?.let {list.addAll(it.getUnitList(identify))}
    return list
  }
  
  override fun close() {
    removeUnit(Any::class.java)
  }
}