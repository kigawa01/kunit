package net.kigawa.kutil.unitimpl.extension.initializedfilter

import net.kigawa.kutil.kutil.reflection.KutilReflect
import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.annotation.Inject
import net.kigawa.kutil.unitapi.component.InitStack
import net.kigawa.kutil.unitapi.component.UnitContainer
import net.kigawa.kutil.unitapi.extention.InitializedFilter
import net.kigawa.kutil.unitapi.options.FindInitGetOption
import net.kigawa.kutil.unitapi.options.FindOptions

class MethodInjectFilter(
  private val container: UnitContainer,
): InitializedFilter {
  override fun <T: Any> filter(obj: T, stack: InitStack): T {
    KutilReflect.getAllExitMethod(obj.javaClass).forEach {method->
      method.getAnnotation(Inject::class.java) ?: return@forEach
      val arg = UnitIdentify.createList(method)
        .map {container.getUnit(it, FindOptions(FindInitGetOption(stack)))}
        .toTypedArray()
      method.isAccessible = true
      method.invoke(obj, *arg)
    }
    return obj
  }
}