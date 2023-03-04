package net.kigawa.kutil.unitimpl.extension.preinitfilter

import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.annotation.Dependencies
import net.kigawa.kutil.unitapi.component.InitStack
import net.kigawa.kutil.unitapi.component.UnitContainer
import net.kigawa.kutil.unitapi.extention.PreInitFilter
import net.kigawa.kutil.unitapi.options.FindInitGetOption
import net.kigawa.kutil.unitapi.options.FindOptions
import net.kigawa.kutil.unitapi.util.ReflectionUtil

class AnnotationPreInitFilter(
  private val container: UnitContainer,
): PreInitFilter {
  override fun <T: Any> filter(identify: UnitIdentify<T>, stack: InitStack) {
    val annotations = ReflectionUtil.getInstanceAnnotation(identify.unitClass, Dependencies::class.java)
    val dependencies = annotations.flatMap {annotation->
      annotation.value.map {UnitIdentify(it.value.java, it.name)}
    }
    container.getCorrespondingUnitList(dependencies, FindOptions(FindInitGetOption(stack)))
  }
}