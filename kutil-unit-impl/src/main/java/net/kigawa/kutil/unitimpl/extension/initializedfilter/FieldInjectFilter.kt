package net.kigawa.kutil.unitimpl.extension.initializedfilter

import net.kigawa.kutil.kutil.reflection.KutilReflect
import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.annotation.Inject
import net.kigawa.kutil.unitapi.component.*
import net.kigawa.kutil.unitapi.exception.NoFoundUnitException
import net.kigawa.kutil.unitapi.extention.InitializedFilter
import java.util.logging.Level

class FieldInjectFilter(
  private val database: UnitDatabaseComponent,
  private val loggerComponent: UnitLoggerComponent,
): InitializedFilter {
  override fun <T: Any> filter(obj: T, stack: InitStack): T {
    KutilReflect.getAllExitFields(obj.javaClass).forEach {
      it.getAnnotation(Inject::class.java) ?: return@forEach
      val identify = UnitIdentify(it.type, it.name)
      val info = database.findOneByEqualsOrClass(identify)
                 ?: throw NoFoundUnitException("field unit is not found", identify = identify)
      it.isAccessible = true
      if (KutilReflect.isFinal(it)) {
        loggerComponent.log(Level.WARNING, "could not inject to final field", null, it)
        return@forEach
      }
      it.set(obj, info.initOrGet(stack))
      
    }
    return obj
  }
}