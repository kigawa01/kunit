package net.kigawa.kutil.unitapi.options

import net.kigawa.kutil.unitapi.annotation.getter.*

enum class RegisterDefaultOption(private val annotationClass: Class<out Annotation>): RegisterOption {
  ALWAYS_INIT(AlwaysInit::class.java),
  LATE_INIT(LateInit::class.java),
  
  @Suppress("unused")
  SINGLETON(Singleton::class.java),
  ;
  
  companion object {
    @JvmStatic
    fun getOption(clazz: Class<out Any>): Array<RegisterDefaultOption> {
      return values().filter {clazz.isAnnotationPresent(it.annotationClass)}.toTypedArray()
    }
  }
  
  override fun match(clazz: Class<out Any>): Boolean {
    return clazz.isAnnotationPresent(annotationClass)
  }
}