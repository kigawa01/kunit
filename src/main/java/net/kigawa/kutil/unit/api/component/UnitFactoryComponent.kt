package net.kigawa.kutil.unit.api.component

import net.kigawa.kutil.unit.component.InitStack
import net.kigawa.kutil.unit.component.UnitIdentify
import net.kigawa.kutil.unit.api.extention.UnitFactory

@Suppress("unused")

interface UnitFactoryComponent {
  fun <T: Any> init(identify: UnitIdentify<T>, stack: InitStack): T
  fun addFactory(factoryClass: Class<out UnitFactory>)
  
  fun removeFactory(factoryClass: Class<out UnitFactory>)
}