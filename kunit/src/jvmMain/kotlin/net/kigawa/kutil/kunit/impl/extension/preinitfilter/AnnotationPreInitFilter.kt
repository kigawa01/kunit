package net.kigawa.kutil.kunit.impl.extension.preinitfilter

import net.kigawa.kutil.kunit.api.UnitIdentify
import net.kigawa.kutil.kunit.api.component.InitStack
import net.kigawa.kutil.kunit.api.component.container.UnitContainer
import net.kigawa.kutil.kunit.api.extention.PreInitFilter
import net.kigawa.kutil.kunit.api.options.FindInitGetOption
import net.kigawa.kutil.kunit.api.options.FindOptions
import net.kigawa.kutil.kutil.reflection.KutilReflect

class AnnotationPreInitFilter(
  private val container: UnitContainer,
): PreInitFilter {
  override fun <T: Any> filter(identify: UnitIdentify<T>, stack: InitStack) {
    val annotations = KutilReflect.getAllExitAnnotation(identify.unitClass, net.kigawa.kutil.kunit.api.annotation.Dependencies::class.java)
    val dependencies = annotations.flatMap {annotation->
      annotation.value.map {UnitIdentify(it.value.java, it.name)}
    }
    dependencies.forEach {
      container.getUnit(it, FindOptions(FindInitGetOption(stack)))
    }
  }
}