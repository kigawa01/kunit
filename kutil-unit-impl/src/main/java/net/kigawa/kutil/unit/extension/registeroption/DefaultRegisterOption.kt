package net.kigawa.kutil.unit.extension.registeroption

import net.kigawa.kutil.unitapi.annotation.getter.*
import net.kigawa.kutil.unitapi.options.RegisterOption

enum class DefaultRegisterOption(private val annotationClass: Class<out Annotation>): RegisterOption {
  ALWAYS_INIT(AlwaysInit::class.java),
  LATE_INIT(LateInit::class.java),
  
  @Suppress("unused")
  SINGLETON(Singleton::class.java),
  ;
  
  companion object {
    @JvmStatic
    fun getOption(clazz: Class<out Any>): Array<DefaultRegisterOption> {
      return values().filter {clazz.isAnnotationPresent(it.annotationClass)}.toTypedArray()
    }
  }
  
  override fun match(clazz: Class<out Any>): Boolean {
    return clazz.isAnnotationPresent(annotationClass)
  }
}