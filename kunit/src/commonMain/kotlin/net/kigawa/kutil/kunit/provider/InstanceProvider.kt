package net.kigawa.kutil.kunit.provider

import net.kigawa.kutil.kunit.dependency.ClassDependency
import kotlin.reflect.KClass

class InstanceProvider<T : Any>(
  private val instance: T,
) : Provider<T> {
  override fun <U : Any> safeCast(superClass: KClass<U>): Provider<out U>? {
    @Suppress("UNCHECKED_CAST")
    if (superClass.isInstance(instance)) return this as Provider<out U>
    return null
  }

  override fun getInstance(): T {
    return instance
  }

  override val instanceClass: KClass<out T>
    get() = instance::class
  override val dependencies: List<ClassDependency<out Any>>
    get() = emptyList()

}