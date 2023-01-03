package net.kigawa.kutil.unit.component.closer

import net.kigawa.kutil.unit.annotation.LateInit
import net.kigawa.kutil.unit.component.container.UnitContainer
import net.kigawa.kutil.unit.component.info.UnitInfo
import net.kigawa.kutil.unit.component.logger.ContainerLoggerComponent
import net.kigawa.kutil.unit.concurrent.ConcurrentList
import net.kigawa.kutil.unit.extension.closer.UnitCloser
import net.kigawa.kutil.unit.extension.database.ComponentInfoDatabase

@LateInit
class UnitCloserComponentImpl(
  private val container: UnitContainer,
  private val loggerComponent: ContainerLoggerComponent,
  private val database: ComponentInfoDatabase,
): UnitCloserComponent {
  private val closerClasses = ConcurrentList<Class<out UnitCloser>>()
  override fun closeUnit(identify: UnitInfo<out Any>) {
    closerClasses.last {
      val closer = loggerComponent.catch(null) {
        container.getUnit(it)
      } ?: return@last false
      return@last loggerComponent.catch(false) {
        closer.closeUnit(identify)
      }
    }
  }
  
  override fun addCloser(closerClass: Class<out UnitCloser>) {
    database.registerComponentClass(closerClass)
    closerClasses.add(closerClass)
  }
  
  override fun removeCloser(closerClass: Class<out UnitCloser>) {
    closerClasses.remove(closerClass)
    database.unregisterComponent(closerClass)
  }
}