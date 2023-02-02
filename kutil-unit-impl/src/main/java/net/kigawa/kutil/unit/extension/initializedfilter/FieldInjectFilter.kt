package net.kigawa.kutil.unit.extension.initializedfilter

import net.kigawa.kutil.unitapi.annotation.Inject
import net.kigawa.kutil.unitapi.component.*
import net.kigawa.kutil.unitapi.exception.UnitException
import net.kigawa.kutil.unitapi.extention.InitializedFilter
import net.kigawa.kutil.unitapi.util.ReflectionUtil

class FieldInjectFilter(
  private val database: UnitDatabaseComponent,
): InitializedFilter {
  override fun <T: Any> filter(obj: T, stack: InitStack): T {
    ReflectionUtil.getInstanceFields(obj.javaClass).forEach {
      if (ReflectionUtil.isStatic(it)) return@forEach
      it.getAnnotation(Inject::class.java) ?: return@forEach
      val identify = UnitIdentify(it.type, it.name)
      val info = database.findOneByEqualsOrClass(identify) ?: throw UnitException("unit is not found", it, identify)
      it.isAccessible = true
      it.set(obj, info.initOrGet(stack).get())
    }
    return obj
  }
}