package net.kigawa.kutil.unit.component.factory

import net.kigawa.kutil.unit.component.info.UnitInfo
import net.kigawa.kutil.unit.extension.factory.UnitFactory
import net.kigawa.kutil.unit.extension.identify.UnitIdentify

@Suppress("unused")

interface UnitFactoryComponent {
  fun dependencies(identify: UnitIdentify<*>): List<UnitInfo<*>>
  
  fun registerFactory(unitFactory: UnitFactory, name: String?)
  fun registerFactory(unitFactory: UnitFactory) {
    registerFactory(unitFactory, null)
  }
  
  fun removeFactory(factoryClass: Class<out UnitFactory>, name: String?)
  
  fun removeFactory(factoryClass: Class<out UnitFactory>) {
    removeFactory(factoryClass, null)
  }
}