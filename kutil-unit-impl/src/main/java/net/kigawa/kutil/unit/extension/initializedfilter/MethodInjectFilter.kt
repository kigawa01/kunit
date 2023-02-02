package net.kigawa.kutil.unit.extension.initializedfilter

import net.kigawa.kutil.unitapi.annotation.Inject
import net.kigawa.kutil.unitapi.component.InitStack
import net.kigawa.kutil.unitapi.component.UnitInjectorComponent
import net.kigawa.kutil.unitapi.extention.InitializedFilter
import net.kigawa.kutil.unitapi.util.ReflectionUtil

class MethodInjectFilter(
  private val injectorComponent: UnitInjectorComponent,
): InitializedFilter {
  override fun <T: Any> filter(obj: T, stack: InitStack): T {
    ReflectionUtil.getInstanceMethod(obj.javaClass).forEach {
      if (ReflectionUtil.isStatic(it)) return@forEach
      it.getAnnotation(Inject::class.java) ?: return@forEach
      val arg = injectorComponent.findUnits(it, stack).toTypedArray()
      it.isAccessible = true
      it.invoke(obj, *arg)
    }
    return obj
  }
}