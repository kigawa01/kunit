package net.kigawa.kutil.unit.component

import net.kigawa.kutil.unit.component.closer.UnitCloserComponent
import net.kigawa.kutil.unit.component.closer.UnitCloserComponentImpl
import net.kigawa.kutil.unit.component.container.UnitContainer
import net.kigawa.kutil.unit.extension.database.UnitInfoDatabase
import net.kigawa.kutil.unit.extension.database.UnitInfoDatabaseImpl
import net.kigawa.kutil.unit.component.initializer.UnitInitializerImpl

class UnitContainerConponentsImpl(override val container: UnitContainer): UnitContainerConponents {
  override var timeoutSec: Long = 10
  override var database: UnitInfoDatabase = UnitInfoDatabaseImpl()
  override var closerComponent: UnitCloserComponent = UnitCloserComponentImpl()
  override var resolver: DependencyResolver = DependencyResolver()
  override var initializer: UnitInitializerImpl = UnitInitializerImpl(this)
  override var register: UnitRegister = UnitRegisterImpl()
  override var executor: (Runnable)->Any = {it.run()}
}