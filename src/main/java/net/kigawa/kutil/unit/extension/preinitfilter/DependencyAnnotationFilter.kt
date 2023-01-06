package net.kigawa.kutil.unit.extension.preinitfilter

import net.kigawa.kutil.unit.annotation.Dependencies
import net.kigawa.kutil.unit.api.component.UnitInjectorComponent
import net.kigawa.kutil.unit.api.extention.PreInitFilter
import net.kigawa.kutil.unit.component.InitStack
import net.kigawa.kutil.unit.component.UnitIdentify

class DependencyAnnotationFilter(
  private val injectorComponent: UnitInjectorComponent,
): PreInitFilter {
  override fun <T: Any> filter(identify: UnitIdentify<T>, stack: InitStack) {
    val annotation = identify.unitClass.getAnnotation(Dependencies::class.java) ?: return
    injectorComponent.findUnits(annotation.value.map {
      UnitIdentify(it.value.java, it.name)
    }, stack)
  }
}