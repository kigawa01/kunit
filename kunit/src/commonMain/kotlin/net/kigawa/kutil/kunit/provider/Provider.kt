package net.kigawa.kutil.kunit.provider

import kotlin.reflect.KClass

interface Provider<T : Any> {
  val instanceClass: KClass<out T>
  fun isInstanceOf(superClass: KClass<out Any>): Boolean
}