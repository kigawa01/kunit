package net.kigawa.kutil.unit.component

import net.kigawa.kutil.unit.component.closer.UnitCloserComponent
import net.kigawa.kutil.unit.component.container.UnitContainer
import net.kigawa.kutil.unit.component.database.UnitDatabase
import net.kigawa.kutil.unit.component.initializer.UnitInitializerImpl
import net.kigawa.kutil.unit.component.register.UnitRegister
import net.kigawa.kutil.unit.component.resolver.DependencyResolver

interface UnitContainerConponents {
  var timeoutSec: Long
  var executor: ((Runnable)->Any)
  var database: UnitDatabase
  var closerComponent: UnitCloserComponent
  var resolver: DependencyResolver
  var initializer: UnitInitializerImpl
  var register: UnitRegister
  val container: UnitContainer
}