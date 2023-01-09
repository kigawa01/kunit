package net.kigawa.kutil.unit.extension.preinitfilter

import net.kigawa.kutil.unit.annotation.Dependencies
import net.kigawa.kutil.unit.api.component.UnitInjectorComponent
import net.kigawa.kutil.unit.api.extention.PreInitFilter
import net.kigawa.kutil.unit.component.InitStack
import net.kigawa.kutil.unit.component.UnitIdentify
import net.kigawa.kutil.unit.util.ReflectionUtil

class DependencyAnnotationFilter(
  private val injectorComponent: UnitInjectorComponent,
): PreInitFilter {
  override fun <T: Any> filter(identify: UnitIdentify<T>, stack: InitStack) {
    val annotations = ReflectionUtil.getInstanceAnnotation(identify.unitClass, Dependencies::class.java)
    val dependencies = annotations.flatMap {annotation->
      annotation.value.map {UnitIdentify(it.value.java, it.name)}
    }
    injectorComponent.findUnits(dependencies, stack)
  }
}