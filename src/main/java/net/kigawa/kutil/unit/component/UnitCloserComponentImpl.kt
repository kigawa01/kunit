package net.kigawa.kutil.unit.component

import net.kigawa.kutil.unit.annotation.getter.LateInit
import net.kigawa.kutil.unit.api.component.*
import net.kigawa.kutil.unit.api.extention.ComponentInfoDatabase
import net.kigawa.kutil.unit.api.extention.UnitCloser
import net.kigawa.kutil.unit.concurrent.ConcurrentList

@LateInit
class UnitCloserComponentImpl(
  private val container: UnitContainer,
  private val loggerComponent: UnitLoggerComponent,
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