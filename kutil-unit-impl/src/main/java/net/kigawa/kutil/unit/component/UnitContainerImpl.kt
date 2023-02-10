package net.kigawa.kutil.unit.component

import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.annotation.getter.LateInit
import net.kigawa.kutil.unitapi.component.*
import net.kigawa.kutil.unitapi.exception.NoFoundUnitException
import net.kigawa.kutil.unitapi.exception.NoSingleUnitException
import net.kigawa.kutil.unitapi.options.FindOptionEnum
import net.kigawa.kutil.unitapi.options.FindOptions
import net.kigawa.kutil.unitapi.util.Util

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
    if (!findOptions.contain(FindOptionEnum.SKIP_PARENT)) parent?.let {list.addAll(it.getUnitList(identify))}
    return list
  }
  
  override fun <T: Any> getUnit(identify: UnitIdentify<T>, findOptions: FindOptions): T {
    val units = getUnitList(
      identify,
      FindOptions(*Util.connectList(findOptions.options, listOf(FindOptionEnum.SKIP_PARENT)).toTypedArray())
    )
    if (parent != null && units.isEmpty() && !findOptions.contain(FindOptionEnum.SKIP_PARENT)) {
      return parent.getUnit(identify, findOptions)
    }
    if (units.isEmpty())
      throw NoFoundUnitException("unit is not found", identify)
    if (units.size == 1) {
      return units[0]
    }
    throw NoSingleUnitException("unit is not single count: ${units.size}", identify)
  }
  
  override fun close() {
    removeUnit(Any::class.java)
  }
}