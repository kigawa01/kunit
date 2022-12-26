package net.kigawa.kutil.unit.extension.registeroption

import net.kigawa.kutil.unit.annotation.*

enum class DefaultRegisterOption(private val annotationClass: Class<out Annotation>): RegisterOption {
  COMPONENT(UnitComponent::class.java),
  ALWAYS_INIT(AlwaysInit::class.java),
  LATE_INIT(LateInit::class.java),
  SINGLETON(Singleton::class.java),
  ;
  
  override fun match(clazz: Class<out Any>): Boolean {
    return clazz.isAnnotationPresent(annotationClass)
  }
}