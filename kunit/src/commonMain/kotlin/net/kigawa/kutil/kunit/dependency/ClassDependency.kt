package net.kigawa.kutil.kunit.dependency

import kotlin.reflect.KClass

interface ClassDependency<T : Any> {
  val instanceClass: KClass<out T>
  val dependencies: List<ClassDependency<out Any>>
  fun isContainDependencies(superClass: KClass<out Any>): Boolean {
    if (instanceClass == superClass) return true
    dependencies.forEach {
      if (it.isContainDependencies(superClass)) return true
    }
    return false
  }
}