package net.kigawa.kutil.unit.component

import net.kigawa.kutil.unit.component.closer.UnitCloserComponent
import net.kigawa.kutil.unit.component.container.UnitContainer
import net.kigawa.kutil.unit.component.database.UnitInfoDatabaseComponent
import net.kigawa.kutil.unit.component.factory.UnitFactoryComponent
import net.kigawa.kutil.unit.component.registrar.UnitRegistrarComponent

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
  override var databaseComponent: UnitInfoDatabaseComponent
    get() = conponents.databaseComponent
    set(value) {
      conponents.databaseComponent = value
    }
  override var closerComponent: UnitCloserComponent
    get() = conponents.closerComponent
    set(value) {
      conponents.closerComponent = value
    }
  override var factoryComponent: UnitFactoryComponent
    get() = conponents.factoryComponent
    set(value) {
      conponents.factoryComponent = value
    }
  override var registerComponent: UnitRegistrarComponent
    get() = conponents.registerComponent
    set(value) {
      conponents.registerComponent = value
    }
  override val container: UnitContainer
    get() = conponents.container
}