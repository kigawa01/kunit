package net.kigawa.kutil.kunit.impl.component

import net.kigawa.kutil.kunit.api.UnitIdentify
import net.kigawa.kutil.kunit.api.annotation.getter.LateInit
import net.kigawa.kutil.kunit.api.component.UnitCloserComponent
import net.kigawa.kutil.kunit.api.component.UnitDatabaseComponent
import net.kigawa.kutil.kunit.api.component.UnitFinderComponent
import net.kigawa.kutil.kunit.api.component.UnitInfo
import net.kigawa.kutil.kunit.api.component.container.UnitContainer
import net.kigawa.kutil.kunit.api.exception.NoSingleUnitException
import net.kigawa.kutil.kunit.api.options.FindOptionEnum
import net.kigawa.kutil.kunit.api.options.FindOptions
import net.kigawa.kutil.kutil.list.KutilList

@LateInit
class UnitContainerImpl(
  val name: String,
  private vararg val parent: UnitContainer,
) : UnitContainer {
  lateinit var closerComponent: UnitCloserComponent
  lateinit var databaseComponent: UnitDatabaseComponent
  lateinit var finderComponent: UnitFinderComponent
  private var closed = false

  init {
    net.kigawa.kutil.kunit.impl.component.ContainerInitializer(this)
  }

  override fun removeUnit(identify: UnitIdentify<out Any>) {
    databaseComponent.findByIdentify(identify).forEach { info ->
      removeInfo(info)
    }
  }

  private fun removeInfo(info: UnitInfo<out Any>) {
    databaseComponent.unregisterInfo(info)
    closerComponent.closeUnit(info)
  }

  override fun <T : Any> getUnitList(identify: UnitIdentify<T>, findOptions: FindOptions): List<T> {
    val list = finderComponent.findUnits(identify, findOptions).toMutableList()
    if (!findOptions.contain(FindOptionEnum.SKIP_PARENT)) parent.forEach { list.addAll(it.getUnitList(identify)) }
    return list
  }

  override fun <T : Any> getUnitOrNull(identify: UnitIdentify<T>, findOptions: FindOptions): T? {
    var units = getUnitList(
      identify,
      FindOptions(*KutilList.connectList(findOptions.options, listOf(FindOptionEnum.SKIP_PARENT)).toTypedArray())
    )
    if (units.isEmpty() && !findOptions.contain(FindOptionEnum.SKIP_PARENT)) {
      units = parent.map { it.getUnit(identify, findOptions) }
    }
    if (units.isEmpty())
      return null
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