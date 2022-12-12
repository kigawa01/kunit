package net.kigawa.kutil.unit.component

import net.kigawa.kutil.unit.component.UnitContainerConponents
import net.kigawa.kutil.unit.component.closer.UnitCloserComponent
import net.kigawa.kutil.unit.component.closer.UnitCloserComponentImpl
import net.kigawa.kutil.unit.component.container.UnitContainer
import net.kigawa.kutil.unit.component.database.UnitDatabase
import net.kigawa.kutil.unit.component.database.UnitDatabaseImpl
import net.kigawa.kutil.unit.component.initializer.UnitInitializerImpl
import net.kigawa.kutil.unit.component.register.UnitRegister
import net.kigawa.kutil.unit.component.register.UnitRegisterImpl
import net.kigawa.kutil.unit.component.resolver.DependencyResolver

class UnitContainerConponentsImpl(override val container: UnitContainer): UnitContainerConponents {
  override var timeoutSec: Long = 10
  override var database: UnitDatabase = UnitDatabaseImpl()
  override var closerComponent: UnitCloserComponent = UnitCloserComponentImpl()
  override var resolver: DependencyResolver = DependencyResolver()
  override var initializer: UnitInitializerImpl = UnitInitializerImpl(this)
  override var register: UnitRegister = UnitRegisterImpl()
  override var executor: (Runnable)->Any = {it.run()}
}