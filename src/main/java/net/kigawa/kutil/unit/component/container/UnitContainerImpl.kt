package net.kigawa.kutil.unit.component.container

import net.kigawa.kutil.unit.component.closer.UnitCloserComponent
import net.kigawa.kutil.unit.component.database.UnitInfoDatabaseComponentImpl
import net.kigawa.kutil.unit.component.info.UnitInfo
import net.kigawa.kutil.unit.component.logger.ContainerLoggerComponent
import net.kigawa.kutil.unit.concurrent.ConcurrentList
import net.kigawa.kutil.unit.extension.identify.UnitIdentify

class UnitContainerImpl(
  private val parent: UnitContainer? = null,
): UnitContainer {
  private val database = UnitInfoDatabaseComponentImpl(this)
  private val closerComponent = getUnit(UnitCloserComponent::class.java)
  private val componentClasses = ConcurrentList<Class<out Any>>()
  private val loggerComponent = getUnit(ContainerLoggerComponent::class.java)
  override fun removeUnit(identify: UnitIdentify<out Any>) {
    database.findByIdentify(identify).filter {info->
      if (componentClasses.contain {info.instanceOf(it)}) return@filter true
      loggerComponent.catch(null) {
        removeInfo(info)
      }
      false
    }
  }
  
  private fun removeInfo(info: UnitInfo<out Any>) {
    database.unregisterInfo(info)
    closerComponent.closeUnit(info)
  }
  
  override fun <T: Any> getUnitList(identify: UnitIdentify<T>): List<T> {
    val list = mutableListOf<T>()
    database.findByIdentify(identify).forEach {
      loggerComponent.catch(null) {
        list.add(it.get())
      }
    }
    parent?.let {list.addAll(it.getUnitList(identify))}
    return list
  }
  
  override fun close() {
    removeUnit(Any::class.java)
    parent?.close()
  }
}