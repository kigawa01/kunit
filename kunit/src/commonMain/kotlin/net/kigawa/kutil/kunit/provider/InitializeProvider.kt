package net.kigawa.kutil.kunit.provider

import net.kigawa.kutil.kunit.dependency.ClassDependency
import kotlin.reflect.KClass

class InitializeProvider<T : Any>(
  override val instanceClass: KClass<out T>,
) : Provider<T> {
  override val dependencies: List<ClassDependency<out Any>>
    get() = emptyList()

  override fun <U : Any> safeCast(superClass: KClass<out Any>): Provider<out U>? {
    TODO("Not yet implemented")
  }

  override fun getInstance(): T {
    TODO("Not yet implemented")
  }

}