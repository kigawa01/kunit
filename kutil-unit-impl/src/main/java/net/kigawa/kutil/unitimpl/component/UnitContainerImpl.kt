package net.kigawa.kutil.unitimpl.component

import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.annotation.getter.LateInit
import net.kigawa.kutil.unitapi.component.*
import net.kigawa.kutil.unitapi.exception.NoFoundUnitException
import net.kigawa.kutil.unitapi.exception.NoSingleUnitException
import net.kigawa.kutil.unitapi.extention.Message
import net.kigawa.kutil.unitapi.options.FindOptionEnum
import net.kigawa.kutil.unitapi.options.FindOptions
import net.kigawa.kutil.unitapi.util.Util
import net.kigawa.kutil.unitimpl.util.LocaleBuilder
import java.util.*
import java.util.logging.Level

@LateInit
class UnitContainerImpl(
  val name: String,
  private vararg val parent: UnitContainer,
): UnitContainer {
  lateinit var closerComponent: UnitCloserComponent
  lateinit var loggerComponent: UnitLoggerComponent
  lateinit var databaseComponent: UnitDatabaseComponent
  lateinit var finderComponent: UnitFinderComponent
  private var closed = false
  
  init {
    ContainerInitializer(this)
  }
  
  override fun removeUnit(identify: UnitIdentify<out Any>) {
    databaseComponent.findByIdentify(identify).forEach {info->
      try {
        removeInfo(info)
      } catch (e: Throwable) {
        loggerComponent.log(
          Message(
            Level.WARNING,
            LocaleBuilder(Locale.ENGLISH, "there an exception when remove unit").toString(),
            listOf(e),
            listOf(identify, info)
          )
        )
      }
    }
  }
  
  private fun removeInfo(info: UnitInfo<out Any>) {
    databaseComponent.unregisterInfo(info)
    closerComponent.closeUnit(info)
  }
  
  override fun <T: Any> getUnitList(identify: UnitIdentify<T>, findOptions: FindOptions): List<T> {
    val list = finderComponent.findUnits(identify, findOptions).toMutableList()
    if (!findOptions.contain(FindOptionEnum.SKIP_PARENT)) parent.forEach {list.addAll(it.getUnitList(identify))}
    return list
  }
  
  override fun <T: Any> getUnit(identify: UnitIdentify<T>, findOptions: FindOptions): T {
    var units = getUnitList(
      identify,
      FindOptions(*Util.connectList(findOptions.options, listOf(FindOptionEnum.SKIP_PARENT)).toTypedArray())
    )
    if (units.isEmpty() && !findOptions.contain(FindOptionEnum.SKIP_PARENT)) {
      units = parent.map {it.getUnit(identify, findOptions)}
    }
    if (units.isEmpty())
      throw NoFoundUnitException("unit is not found", identify = identify)
    if (units.size == 1) {
      return units[0]
    }
    throw NoSingleUnitException("unit is not single count", identify = identify, units = units)
  }
  
  @Synchronized
  override fun close() {
    if (closed) return
    closed = true
    removeUnit(Any::class.java)
  }
}