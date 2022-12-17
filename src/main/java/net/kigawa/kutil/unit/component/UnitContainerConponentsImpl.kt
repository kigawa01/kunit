package net.kigawa.kutil.unit.component

import net.kigawa.kutil.unit.component.closer.UnitCloserComponent
import net.kigawa.kutil.unit.component.closer.UnitCloserComponentImpl
import net.kigawa.kutil.unit.component.container.UnitContainer
import net.kigawa.kutil.unit.component.database.UnitInfoDatabaseComponent
import net.kigawa.kutil.unit.component.database.UnitInfoDatabaseComponentImpl
import net.kigawa.kutil.unit.component.factory.UnitFactoryComponent
import net.kigawa.kutil.unit.component.factory.UnitFactoryComponentImpl
import net.kigawa.kutil.unit.component.getter.UnitGetterComponent
import net.kigawa.kutil.unit.component.logger.ContainerLoggerComponent

class UnitContainerConponentsImpl(override val container: UnitContainer): UnitContainerConponents {
  override var timeoutSec: Long = 10
  override var closerComponent: UnitCloserComponent = UnitCloserComponentImpl()
  override var factoryComponent: UnitFactoryComponent = UnitFactoryComponentImpl()
  override var registerComponent: UnitRegistrarComponent = UnitRegistrarComponentImpl(this)
  override var getterComponent: UnitGetterComponent = TODO()
  override var logger: ContainerLoggerComponent = TODO()
  override var executor: (Runnable)->Any = {it.run()}
  override var databaseComponent: UnitInfoDatabaseComponent = UnitInfoDatabaseComponentImpl()
}