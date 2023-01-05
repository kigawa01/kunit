package net.kigawa.kutil.unit.extension.factory

import net.kigawa.kutil.unit.annotation.getter.LateInit
import net.kigawa.kutil.unit.component.InitStack
import net.kigawa.kutil.unit.component.UnitIdentify

@LateInit
class KotlinObjectFactory: UnitFactory {
  override fun <T: Any> init(identify: UnitIdentify<T>, initStack: InitStack): T? {
    if (!identify.unitClass.isAnnotationPresent(Metadata::class.java)) return null
    
    return try {
      val field = identify.unitClass.getField("INSTANCE")
      @Suppress("UNCHECKED_CAST")
      field[null] as T
    } catch (e: NoSuchFieldException) {
      null
    } catch (e: IllegalAccessException) {
      null
    }
  }
}