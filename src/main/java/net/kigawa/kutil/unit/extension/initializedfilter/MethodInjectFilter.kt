package net.kigawa.kutil.unit.extension.initializedfilter

import net.kigawa.kutil.unit.annotation.Inject
import net.kigawa.kutil.unit.api.component.UnitInjectorComponent
import net.kigawa.kutil.unit.api.extention.InitializedFilter
import net.kigawa.kutil.unit.component.InitStack
import net.kigawa.kutil.unit.util.ReflectionUtil

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