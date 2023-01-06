package net.kigawa.kutil.unit.extension.initializedfilter

import net.kigawa.kutil.unit.annotation.Inject
import net.kigawa.kutil.unit.api.component.UnitDatabaseComponent
import net.kigawa.kutil.unit.api.extention.InitializedFilter
import net.kigawa.kutil.unit.component.InitStack
import net.kigawa.kutil.unit.component.UnitIdentify
import net.kigawa.kutil.unit.exception.UnitException

class FieldInjectFilter(
  private val database: UnitDatabaseComponent,
): InitializedFilter {
  override fun <T: Any> filter(obj: T): T {
    obj.javaClass.declaredFields.forEach {
      val annotation = it.getAnnotation(Inject::class.java) ?: return@forEach
      val identify = UnitIdentify(it.type, it.name)
      val info = database.findOneByEqualsOrClass(identify) ?: throw UnitException("unit is not found", it, identify)
      it.isAccessible = true
      it.set(obj, info.initOrGet(InitStack()).get())
    }
    return obj
  }
}