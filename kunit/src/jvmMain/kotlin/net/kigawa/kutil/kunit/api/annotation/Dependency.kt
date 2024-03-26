package net.kigawa.kutil.kunit.api.annotation

import kotlin.reflect.KClass

@JvmRepeatable(value = Dependencies::class)
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Dependency
  (val value: KClass<*>, val name: String = "")
