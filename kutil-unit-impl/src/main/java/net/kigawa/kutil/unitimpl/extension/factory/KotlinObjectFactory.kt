package net.kigawa.kutil.unitimpl.extension.factory

import net.kigawa.kutil.kutil.reflection.KutilReflect
import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.annotation.getter.LateInit
import net.kigawa.kutil.unitapi.component.InitStack
import net.kigawa.kutil.unitapi.extention.UnitFactory

@LateInit
class KotlinObjectFactory : UnitFactory {
  override fun <T : Any> init(identify: UnitIdentify<T>, initStack: InitStack): T? {
    if (!identify.unitClass.isAnnotationPresent(Metadata::class.java)) return null

    return try {
      @Suppress("UNCHECKED_CAST")
      val instance = KutilReflect.getAllExitFields(identify.unitClass)
        .firstOrNull { it.name == "INSTANCE" }
        ?.get(null) as T?
      instance
    } catch (e: NoSuchFieldException) {
      null
    } catch (e: IllegalAccessException) {
      null
    }
  }
}