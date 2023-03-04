package net.kigawa.kutil.unitimpl.extension.initializedfilter

import net.kigawa.kutil.unitapi.UnitIdentifies
import net.kigawa.kutil.unitapi.annotation.Inject
import net.kigawa.kutil.unitapi.component.InitStack
import net.kigawa.kutil.unitapi.component.UnitContainer
import net.kigawa.kutil.unitapi.extention.InitializedFilter
import net.kigawa.kutil.unitapi.options.FindInitGetOption
import net.kigawa.kutil.unitapi.options.FindOptions
import net.kigawa.kutil.unitapi.util.ReflectionUtil

class MethodInjectFilter(
  private val container: UnitContainer,
): InitializedFilter {
  override fun <T: Any> filter(obj: T, stack: InitStack): T {
    ReflectionUtil.getInstanceMethod(obj.javaClass).forEach {
      if (ReflectionUtil.isStatic(it)) return@forEach
      it.getAnnotation(Inject::class.java) ?: return@forEach
      val arg =
        container.getCorrespondingUnitList(UnitIdentifies.createList(it), FindOptions(FindInitGetOption(stack)))
          .toTypedArray()
      it.isAccessible = true
      it.invoke(obj, *arg)
    }
    return obj
  }
}