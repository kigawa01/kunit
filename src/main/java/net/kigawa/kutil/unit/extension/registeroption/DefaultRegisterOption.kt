package net.kigawa.kutil.unit.extension.registeroption

import net.kigawa.kutil.unit.annotation.getter.*
import net.kigawa.kutil.unit.api.extention.RegisterOption

enum class DefaultRegisterOption(private val annotationClass: Class<out Annotation>): RegisterOption {
  ALWAYS_INIT(AlwaysInit::class.java),
  LATE_INIT(LateInit::class.java),
  @Suppress("unused")
  SINGLETON(Singleton::class.java),
  ;
  
  override fun match(clazz: Class<out Any>): Boolean {
    return clazz.isAnnotationPresent(annotationClass)
  }
}