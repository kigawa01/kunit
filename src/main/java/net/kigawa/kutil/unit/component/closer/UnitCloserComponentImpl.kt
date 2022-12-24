package net.kigawa.kutil.unit.component.closer

import net.kigawa.kutil.unit.component.container.UnitContainer
import net.kigawa.kutil.unit.component.info.UnitInfo
import net.kigawa.kutil.unit.component.logger.ContainerLoggerComponent
import net.kigawa.kutil.unit.concurrent.UnitClassList
import net.kigawa.kutil.unit.extension.closer.UnitCloser

class UnitCloserComponentImpl(
  private val container: UnitContainer,
  private val loggerComponent: ContainerLoggerComponent,
): UnitCloserComponent {
  private val closerClasses = UnitClassList<UnitCloser>(container)
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
    closerClasses.addContainer(closerClass)
  }
  
  override fun removeCloser(closerClass: Class<out UnitCloser>) {
    closerClasses.removeContainer(closerClass)
  }
}