package net.kigawa.kutil.unit.component.closer

import net.kigawa.kutil.unit.component.factory.UnitFactoryComponent
import net.kigawa.kutil.unit.extension.closer.UnitCloser

class UnitCloserComponentImpl: UnitCloserComponent {
  fun addCloser(closer: UnitCloser, name: String?)
  fun addCloser(closer: UnitCloser) {
    addCloser(closer, null)
  }
  fun removeCloser(closerClass: Class<out UnitCloser>, name: String?): MutableList<Throwable>
  
  @Suppress("unused")
  fun removeCloser(closerClass: Class<out UnitCloser>): MutableList<Throwable> {
    return removeCloser(closerClass, null)
  }
  
}