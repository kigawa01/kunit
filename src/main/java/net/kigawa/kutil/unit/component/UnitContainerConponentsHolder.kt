package net.kigawa.kutil.unit.component

import net.kigawa.kutil.unit.component.closer.UnitCloserComponent
import net.kigawa.kutil.unit.component.container.UnitContainer
import net.kigawa.kutil.unit.component.database.UnitDatabase
import net.kigawa.kutil.unit.component.initializer.UnitInitializerImpl
import net.kigawa.kutil.unit.component.register.UnitRegister
import net.kigawa.kutil.unit.component.resolver.DependencyResolver

interface UnitContainerConponentsHolder: UnitContainerConponents {
  
  var conponents: UnitContainerConponents
  override var timeoutSec: Long
    get() = conponents.timeoutSec
    set(value) {
      conponents.timeoutSec = value
    }
  override var executor: (Runnable)->Any
    get() = conponents.executor
    set(value) {
      conponents.executor = value
    }
  override var database: UnitDatabase
    get() = conponents.database
    set(value) {
      conponents.database = value
    }
  override var closerComponent: UnitCloserComponent
    get() = conponents.closerComponent
    set(value) {
      conponents.closerComponent = value
    }
  override var resolver: DependencyResolver
    get() = conponents.resolver
    set(value) {
      conponents.resolver = value
    }
  override var initializer: UnitInitializerImpl
    get() = conponents.initializer
    set(value) {
      conponents.initializer = value
    }
  override var register: UnitRegister
    get() = conponents.register
    set(value) {
      conponents.register = value
    }
  override val container: UnitContainer
    get() = conponents.container
}