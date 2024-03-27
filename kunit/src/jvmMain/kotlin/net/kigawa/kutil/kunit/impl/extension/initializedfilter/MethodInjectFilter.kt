package net.kigawa.kutil.kunit.impl.extension.initializedfilter

import net.kigawa.kutil.kunit.api.UnitIdentify
import net.kigawa.kutil.kunit.api.component.InitStack
import net.kigawa.kutil.kunit.api.component.container.UnitContainer
import net.kigawa.kutil.kunit.api.extention.InitializedFilter
import net.kigawa.kutil.kunit.api.options.FindInitGetOption
import net.kigawa.kutil.kunit.api.options.FindOptions
import net.kigawa.kutil.kutil.api.reflection.KutilReflect

class MethodInjectFilter(
  private val container: UnitContainer,
): InitializedFilter {
  override fun <T: Any> filter(obj: T, stack: InitStack): T {
    KutilReflect.getAllExitMethod(obj.javaClass).forEach { method->
      method.getAnnotation(net.kigawa.kutil.kunit.api.annotation.Inject::class.java) ?: return@forEach
      val arg = UnitIdentify.createList(method)
        .map {container.getUnit(it, FindOptions(FindInitGetOption(stack)))}
        .toTypedArray()

      method.isAccessible = true
      method.invoke(obj, *arg)
    }
    return obj
  }
}