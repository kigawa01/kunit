package net.kigawa.kutil.unit.component.factory

import net.kigawa.kutil.unit.extension.factory.UnitFactory
import net.kigawa.kutil.unit.extension.identify.UnitIdentify

@Suppress("unused")

interface UnitFactoryComponent {
  fun <T: Any> init(identify: UnitIdentify<T>, stack: InitStack): T
  fun addFactory(factoryClass: Class<out UnitFactory>)
  
  fun removeFactory(factoryClass: Class<out UnitFactory>)
}