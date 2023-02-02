package net.kigawa.kutil.unit.extension.factory

import net.kigawa.kutil.unitapi.annotation.getter.LateInit
import net.kigawa.kutil.unitapi.component.InitStack
import net.kigawa.kutil.unitapi.component.UnitIdentify
import net.kigawa.kutil.unitapi.extention.UnitFactory

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