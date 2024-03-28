package net.kigawa.kutil.kunit.exception

import kotlin.reflect.KClass

class NoSingleConstructorException(instanceClass: KClass<out Any>) : Exception()