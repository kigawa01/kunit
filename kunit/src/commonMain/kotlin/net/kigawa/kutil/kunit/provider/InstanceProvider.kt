package net.kigawa.kutil.kunit.provider

import kotlin.reflect.KClass

class InstanceProvider<T : Any>(
  private val instance: T,
) : Provider<T> {
  override val instanceClass: KClass<out T>
    get() = instance::class

  override fun isInstanceOf(superClass: KClass<out Any>): Boolean {
    return superClass.isInstance(instance)
  }
}