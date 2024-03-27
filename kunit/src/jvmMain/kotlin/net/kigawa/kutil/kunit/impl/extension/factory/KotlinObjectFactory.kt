package net.kigawa.kutil.kunit.impl.extension.factory

import net.kigawa.kutil.kunit.api.UnitIdentify
import net.kigawa.kutil.kunit.api.annotation.getter.LateInit
import net.kigawa.kutil.kunit.api.component.InitStack
import net.kigawa.kutil.kunit.api.extention.UnitFactory
import net.kigawa.kutil.kutil.api.reflection.KutilReflect

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