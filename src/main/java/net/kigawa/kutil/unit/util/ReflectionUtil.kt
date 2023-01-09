package net.kigawa.kutil.unit.util

import java.lang.reflect.*

object ReflectionUtil {
  fun getInstanceFields(clazz: Class<*>): List<Field> {
    return getAllClasses(clazz)
      .flatMap {it.declaredFields.asList()}
      .filter {!isStatic(it)}
  }
  
  fun getInstanceMethod(clazz: Class<*>): List<Method> {
    return getAllClasses(clazz)
      .flatMap {it.declaredMethods.asList()}
      .filter {!isStatic(it)}
  }
  
  fun <T: Annotation> getInstanceAnnotation(clazz: Class<*>, annotationClass: Class<T>): MutableList<T> {
    val list = mutableListOf<T>()
    getAllClasses(clazz).forEach {
      val annotation = it.getAnnotation(annotationClass) ?: return@forEach
      list.add(annotation)
    }
    return list
  }
  
  fun isStatic(member: Member): Boolean {
    return Modifier.isStatic(member.modifiers)
  }
  
  fun instanceOf(clazz: Class<*>, superClass: Class<*>): Boolean {
    return getAllClasses(clazz).contains(superClass)
  }
  
  fun getAllClasses(clazz: Class<*>): MutableList<Class<*>> {
    val list = mutableListOf(clazz)
    clazz.superclass?.let {list.addAll(getAllClasses(clazz.superclass))}
    clazz.interfaces.forEach {
      list.addAll(getAllClasses(it))
    }
    return list
  }
}
