package net.kigawa.kutil.unit.util

import net.kigawa.kutil.unit.annotation.Kunit
import net.kigawa.kutil.unit.annotation.Unit

object AnnotationUtil {
  fun hasUnitAnnotation(clazz: Class<out Any>): Boolean {
    return clazz.isAnnotationPresent(Unit::class.java) || clazz.isAnnotationPresent(Kunit::class.java)
  }
  fun getUnitNameByAnnotation(clazz: Class<out Any>){
    var name= clazz.getAnnotation(Kunit::class.java)?.name?:""
    if ()
  }
}