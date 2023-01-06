package net.kigawa.kutil.unit.extension.initializedfilter

import net.kigawa.kutil.unit.annotation.Inject
import net.kigawa.kutil.unit.api.component.UnitInjectorComponent
import net.kigawa.kutil.unit.api.extention.InitializedFilter
import net.kigawa.kutil.unit.component.InitStack

class MethodInjectFilter(
  private val injectorComponent: UnitInjectorComponent,
): InitializedFilter {
  override fun <T: Any> filter(obj: T, stack: InitStack): T {
    obj.javaClass.declaredMethods.forEach {
      it.getAnnotation(Inject::class.java) ?: return@forEach
      val arg = injectorComponent.findUnits(it, stack)
      it.isAccessible = true
      it.invoke(obj, arg)
    }
    return obj
  }
}