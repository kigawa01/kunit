package net.kigawa.kutil.unit.component.factory

import net.kigawa.kutil.unit.extension.factory.UnitFactory

interface UnitFactoryComponent {
  
  fun addFactory(unitFactory: UnitFactory, name: String?)
  fun addFactory(unitFactory: UnitFactory) {
    addFactory(unitFactory, null)
  }
  
  fun removeFactory(factoryClass: Class<out UnitFactory>, name: String?)
  
  @Suppress("unused")
  fun removeFactory(factoryClass: Class<out UnitFactory>) {
    removeFactory(factoryClass, null)
  }
  
}