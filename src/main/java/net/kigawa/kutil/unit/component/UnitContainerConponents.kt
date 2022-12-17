package net.kigawa.kutil.unit.component

import net.kigawa.kutil.unit.component.closer.UnitCloserComponent
import net.kigawa.kutil.unit.component.container.UnitContainer
import net.kigawa.kutil.unit.component.database.UnitInfoDatabaseComponent
import net.kigawa.kutil.unit.component.factory.UnitFactoryComponent
import net.kigawa.kutil.unit.component.getter.UnitGetterComponent
import net.kigawa.kutil.unit.component.logger.ContainerLoggerComponent
import net.kigawa.kutil.unit.component.registrar.UnitRegistrarComponent
import net.kigawa.kutil.unit.extension.logger.ContainerLogger

interface UnitContainerConponents {
  var timeoutSec: Long
  var executor: ((Runnable)->Any)
  var databaseComponent: UnitInfoDatabaseComponent
  var closerComponent: UnitCloserComponent
  var factoryComponent: UnitFactoryComponent
  var registerComponent: UnitRegistrarComponent
  var getterComponent: UnitGetterComponent
  var logger: ContainerLoggerComponent
  val container: UnitContainer
}