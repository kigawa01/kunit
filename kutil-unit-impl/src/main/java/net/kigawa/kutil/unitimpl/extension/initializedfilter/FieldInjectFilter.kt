package net.kigawa.kutil.unitimpl.extension.initializedfilter

import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.annotation.Inject
import net.kigawa.kutil.unitapi.component.*
import net.kigawa.kutil.unitapi.exception.NoFoundUnitException
import net.kigawa.kutil.unitapi.extention.InitializedFilter
import net.kigawa.kutil.unitapi.util.ReflectionUtil
import java.util.logging.Level

class FieldInjectFilter(
  private val database: UnitDatabaseComponent,
  private val loggerComponent: UnitLoggerComponent,
): InitializedFilter {
  override fun <T: Any> filter(obj: T, stack: InitStack): T {
    ReflectionUtil.getFields(obj.javaClass).forEach {
      it.getAnnotation(Inject::class.java) ?: return@forEach
      val identify = UnitIdentify(it.type, it.name)
      val info = database.findOneByEqualsOrClass(identify)
                 ?: throw NoFoundUnitException("unit is not found", identify = identify)
      it.isAccessible = true
      if (ReflectionUtil.isFinal(it)) {
        loggerComponent.log(Level.WARNING, "could not inject to final field", null, it)
        return@forEach
      }
      it.set(obj, info.initOrGet(stack))
      
    }
    return obj
  }
}