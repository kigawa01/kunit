package net.kigawa.kutil.kunit.provider

import net.kigawa.kutil.kunit.dependency.ClassDependency
import kotlin.reflect.KClass

interface Provider<T : Any> : ClassDependency<T> {
  fun <U : Any> safeCast(superClass: KClass<out Any>): Provider<out U>?
  fun getInstance(): T
}