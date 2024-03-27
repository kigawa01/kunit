package net.kigawa.kutil.kunit.impl.extension.initializedfilter

import net.kigawa.kutil.kunit.api.UnitIdentify
import net.kigawa.kutil.kunit.api.component.InitStack
import net.kigawa.kutil.kunit.api.component.UnitDatabaseComponent
import net.kigawa.kutil.kunit.api.exception.InjectFinalException
import net.kigawa.kutil.kunit.api.exception.NoFoundUnitException
import net.kigawa.kutil.kunit.api.extention.InitializedFilter
import net.kigawa.kutil.kutil.api.reflection.KutilReflect

class FieldInjectFilter(
  private val database: UnitDatabaseComponent,
) : InitializedFilter {
  override fun <T : Any> filter(obj: T, stack: InitStack): T {
    KutilReflect.getAllExitFields(obj.javaClass).forEach {
      it.getAnnotation(net.kigawa.kutil.kunit.api.annotation.Inject::class.java) ?: return@forEach
      val identify = UnitIdentify(it.type, it.name)
      val info = database.findOneByEqualsOrClass(identify)
        ?: throw NoFoundUnitException("field unit is not found", identify = identify)
      it.isAccessible = true
      if (KutilReflect.isFinal(it)) {
        throw InjectFinalException("could not inject to final field: $it", null)
      }
      it.set(obj, info.initOrGet(stack))

    }
    return obj
  }
}