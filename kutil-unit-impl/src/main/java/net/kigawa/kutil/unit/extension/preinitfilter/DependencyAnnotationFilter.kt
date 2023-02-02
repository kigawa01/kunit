package net.kigawa.kutil.unit.extension.preinitfilter

import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.annotation.Dependencies
import net.kigawa.kutil.unitapi.component.*
import net.kigawa.kutil.unitapi.extention.PreInitFilter
import net.kigawa.kutil.unitapi.options.FindOptions
import net.kigawa.kutil.unitapi.util.ReflectionUtil

class DependencyAnnotationFilter(
  private val injectorComponent: UnitFinderComponent,
): PreInitFilter {
  override fun <T: Any> filter(identify: UnitIdentify<T>, stack: InitStack) {
    val annotations = ReflectionUtil.getInstanceAnnotation(identify.unitClass, Dependencies::class.java)
    val dependencies = annotations.flatMap {annotation->
      annotation.value.map {UnitIdentify(it.value.java, it.name)}
    }
    injectorComponent.findUnits(dependencies, stack, FindOptions())
  }
}