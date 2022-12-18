package net.kigawa.kutil.unit.component.closer

import net.kigawa.kutil.unit.component.container.UnitContainer
import net.kigawa.kutil.unit.component.logger.ContainerLoggerComponent
import net.kigawa.kutil.unit.concurrent.UnitClassList
import net.kigawa.kutil.unit.extension.closer.UnitCloser
import net.kigawa.kutil.unit.extension.identify.UnitIdentify

class UnitCloserComponentImpl(
  private val container: UnitContainer,
  private val loggerComponent: ContainerLoggerComponent,
): UnitCloserComponent {
  private val closerClasses = UnitClassList<UnitCloser>(container)
  override fun closeUnit(identify: UnitIdentify<Any>) {
    closerClasses.last {
      val closer = loggerComponent.catch(null, "") {
        container.getUnit(it)
      } ?: return@last false
      return@last loggerComponent.catch(false, "") {
        closer.closeUnit(identify)
      }
    }
  }
  
  override fun addCloser(closerClass: Class<out UnitCloser>) {
    closerClasses.add(closerClass)
  }
  
  override fun removeCloser(closerClass: Class<out UnitCloser>) {
    closerClasses.remove(closerClass)
  }
}