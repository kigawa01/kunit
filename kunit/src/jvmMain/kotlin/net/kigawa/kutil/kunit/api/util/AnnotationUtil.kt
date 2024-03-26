@file:Suppress("DEPRECATION")

package net.kigawa.kutil.kunit.api.util

import net.kigawa.kutil.kunit.api.annotation.Kunit
import net.kigawa.kutil.kunit.api.annotation.Unit

object AnnotationUtil {
  fun hasUnitAnnotation(clazz: Class<out Any>): Boolean {
    return clazz.isAnnotationPresent(Unit::class.java) || clazz.isAnnotationPresent(
      Kunit::class.java
    )
  }
  
  fun getUnitNameByAnnotation(clazz: Class<out Any>): String? {
    var name = clazz.getAnnotation(Kunit::class.java)?.name
    if (name != null && name != "") return name
    name = clazz.getAnnotation(Unit::class.java)?.name
    if (name != null && name != "") return name
    return null
  }
}